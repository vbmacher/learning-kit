package cz.zoom.whiteboard;

import cz.zoom.whiteboard.task.Task;
import cz.zoom.whiteboard.task.Tasks;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Issue.FluentCreate;
import net.rcarz.jiraclient.Issue.FluentUpdate;
import net.rcarz.jiraclient.Issue.SearchResult;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Status;
import net.rcarz.jiraclient.greenhopper.GreenHopperClient;
import net.rcarz.jiraclient.greenhopper.RapidView;
import net.rcarz.jiraclient.greenhopper.Sprint;
import net.rcarz.jiraclient.greenhopper.SprintIssue;
import net.rcarz.jiraclient.greenhopper.SprintReport;

public class JiraAdapter {
    public static final String ISSUE_KEY = Field.ValueType.KEY.toString();
    
    private final BasicCredentials credentials;
    private final JiraClient jira;
    private final GreenHopperClient greenHopper;
    private final PrintStream out;
    
    public JiraAdapter(ConnectionDetails details, PrintStream out) throws URISyntaxException, IOException {
        this.out = out;
        credentials = new BasicCredentials(details.getUserName(), details.getPassword());
        jira = new JiraClient(details.getURL(), credentials);
        greenHopper = new GreenHopperClient(jira);
    }
    
    public Issue getIssue(String issueKey) throws JiraException {
        return jira.getIssue(issueKey);
    }
    
    private boolean containsIssue(List<Issue> issues, String issueKey, List<Issue> alreadySearched) throws JiraException {
        for (Issue issue : issues) {
            if (alreadySearched != null && alreadySearched.contains(issue)) {
                continue;
            }
            
            if (issue.getKey().equals(issueKey)) {
                return true;
            } else {
                if (alreadySearched == null) {
                    alreadySearched = new ArrayList<Issue>();
                }
                alreadySearched.add(issue);

                SearchResult result = jira.searchIssues("parent=" + issue.getKey());
                if (containsIssue(result.issues, issueKey,alreadySearched)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private List<Issue> jiraIssues(List<SprintIssue> issues) throws JiraException {
        List<Issue> jiraIssues = new ArrayList<Issue>();
        for (SprintIssue issue : issues) {
            jiraIssues.add(issue.getJiraIssue());
        }
        return jiraIssues;
    }
    
    public List<SprintIssue> getOpenSprintIssues(SprintReport report, String issueKey) throws JiraException {
        List<SprintIssue> issues = new ArrayList<SprintIssue>();
        
        List<SprintIssue> issuesToSearch = new ArrayList<SprintIssue>();
        issuesToSearch.addAll(report.getCompletedIssues());
        issuesToSearch.addAll(report.getPuntedIssues());
        issuesToSearch.addAll(report.getIncompletedIssues());
        
        boolean found = containsIssue(jiraIssues(issuesToSearch), issueKey, null);
        
        if (found) {
            for (SprintIssue issue : report.getIncompletedIssues()) {
                if (!issue.isDone()) {
                    issues.add(issue);
                }
            }
            return issues;
        }
        return issues;
    }
    
    public List<SprintIssue> getOpenSprintIssues(int rapidViewID, int sprintID) throws JiraException {
        RapidView rapidView = greenHopper.getRapidView(rapidViewID);
        if (rapidView == null) {
            throw new JiraException("Rapid view was not found");
        }
        
        for (Sprint sprint : rapidView.getSprints()) {
            if (sprint.getId() == sprintID) {
                SprintReport report = rapidView.getSprintReport(sprint);
                return report.getIncompletedIssues();
            }
        }
        return null;
    }

    
    public List<SprintIssue> getOpenSprintIssues(String issueKey) throws JiraException {
        Set<Integer> sprintIds = new HashSet<Integer>();
        for (RapidView view : greenHopper.getRapidViews()) {
            for (Sprint sprint : view.getSprints()) {
                if (sprintIds.contains(sprint.getId())) {
                    continue;
                }
                sprintIds.add(sprint.getId());
                SprintReport report = view.getSprintReport(sprint);
                List<SprintIssue> issues = getOpenSprintIssues(report, issueKey);
                if (!issues.isEmpty()) {
                    return issues;
                }
            }
        }
        return null;
    }
    
    public Issue createIssue(Task task) throws JiraException {
        FluentCreate create = jira.createIssue(
                task.get(Field.PROJECT),
                task.get(Field.ISSUE_TYPE));
        
        for (Entry<String, String> data : task.getFields()) {
            if (data.getKey().equals(Field.PROJECT)) {
                continue;
            }
            if (data.getKey().equals(Field.ISSUE_TYPE)) {
                continue;
            }
            create.field(data.getKey(), data.getValue());
        }
        return create.execute();
    }
    
    public void createIssues(Tasks tasks) throws JiraException {
        for (Task task : tasks.getTasks()) {
            createIssue(task);
        }
    }
    
    private void updateIssue(Issue issue, Task task) throws JiraException {
        FluentUpdate update = issue.update();
        for (Entry<String, String> data : task.getFields()) {
            update.field(data.getKey(), data.getValue());
        }
        update.execute();
    }
    
    public void updateIssues(Tasks tasks) throws JiraException {
        for (Task task : tasks.getTasks()) {
            String key = task.get(ISSUE_KEY);
            SearchResult result = jira.searchIssues(ISSUE_KEY + " = " + key);
            
            if (result.total > 1) {
                throw new JiraException("Issue key " + key + " is ambiguous");
            }
            if (result.total == 0) {
                throw new JiraException("Issue key " + key + " was not found");
            }
            Issue issue = result.issues.get(0);
            updateIssue(issue, task);
        }
    }
    
    private void printNotEqual(String issue, Entry<String, String> field, String what) {
        out.println(issue + " [" + field.getKey() + "] " + what + " != " + field.getValue());
    }
    
    private void printDoesNotExist(String key, Entry<String, String> field) {
        out.println(key + " [" + field.getKey() + "] does not exist");
    }

    public void checkIssue(Task task) throws JiraException {
        Issue issue = getIssue(task.get(ISSUE_KEY));
        if (issue != null) {
            for (Entry<String, String> field : task.getFields()) {
                String issueField = Field.getString(issue.getField(field.getKey()));
                String fieldKey = field.getKey();
                String fieldValue = field.getValue();
                
                if (issueField != null) {
                    if (!issueField.equals(fieldValue)) {
                        printNotEqual(issue.getKey(), field, issueField);
                    }
                } else {
                    if (fieldKey.equals(Field.STATUS)) {
                        Status status = issue.getStatus();
                        if (!status.getName().equals(fieldValue)) {
                            printNotEqual(issue.getKey(), field, status.getName());
                        }
                    } else if (fieldKey.equals(ISSUE_KEY)) {
                        // nothing, they equal.
                    } else {
                        printDoesNotExist(issue.getKey(), field);
                    }
                }
            }
        }
    }
    
    public void checkIssues(Tasks tasks) throws JiraException {
        for (Task task : tasks.getTasks()) {
            checkIssue(task);
        }
    }
    
    private void printWithCheck(String key, String value) {
        if (value != null) {
            out.println(key + ": " + value);
        }
    }
    
    public void printYaml(SprintIssue issue) {
        printWithCheck(ISSUE_KEY, issue.getKey());
        printWithCheck(Field.SUMMARY, issue.getSummary());
        out.println("---");
    }
    
    public void print(SprintIssue issue) {
        out.println("[" + issue.getKey() + "] " + issue.getSummary());
    }

    public void printYaml(Collection<SprintIssue> issues) {
        for (SprintIssue issue : issues) {
            printWithCheck(ISSUE_KEY, issue.getKey());
            printWithCheck(Field.SUMMARY, issue.getSummary());
            out.println("---");
        }
    }

    public void print(Collection<SprintIssue> issues) {
        for (SprintIssue issue : issues) {
            out.println("[" + issue.getKey() + "] " + issue.getSummary());
        }
    }

}
