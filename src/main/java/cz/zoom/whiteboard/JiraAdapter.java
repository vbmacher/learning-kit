package cz.zoom.whiteboard;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Issue.SearchResult;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.GreenHopperClient;
import net.rcarz.jiraclient.greenhopper.RapidView;
import net.rcarz.jiraclient.greenhopper.Sprint;
import net.rcarz.jiraclient.greenhopper.SprintIssue;
import net.rcarz.jiraclient.greenhopper.SprintReport;

public class JiraAdapter {
    public static final String STATE_READY_FOR_ACCEPTACNE = "Ready For Acceptance";
    public static final String STATE_CLOSED = "Closed";
    public static final String STATE_IN_PROGRESS = "In Progress";
    
    public static final String TYPE_TECHNICAL_TASK = "Technical Task";
    public static final String TYPE_BUG = "Bug";

    private final BasicCredentials credentials;
    private final JiraClient jira;
    private final GreenHopperClient greenHopper;
    
    public JiraAdapter(String jiraURL, String userName, String password, boolean useGreenHopper) throws URISyntaxException, IOException {
        if (jiraURL == null) {
            jiraURL = readURL();
        }
        if (userName == null) {
            userName = readUserName();
        }
        if (password == null) {
            password = readPassword();
        }
        credentials = new BasicCredentials(userName, password);
        jira = new JiraClient(jiraURL, credentials);
        if (useGreenHopper) {
            greenHopper = new GreenHopperClient(jira);
        } else {
            greenHopper = null;
        }
    }
    
    private String readUserName() throws IOException {
        System.out.print("Enter user name: ");
        return IOUtils.readString();
    }
    
    private String readPassword() throws IOException {
        System.out.print("Enter password: ");
        return IOUtils.readStringSilently();
    }
    
    private String readURL() throws IOException {
        System.out.print("Enter JIRA URL: ");
        return IOUtils.readString();
    }

    public Issue getIssue(String issueKey) throws JiraException {
        return jira.getIssue(issueKey);
    }
    
    private void checkGreenHopper() {
        if (greenHopper == null) {
            throw new UnsupportedOperationException("GreenHopper JIRA plugin is not accessible");
        }
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
        checkGreenHopper();
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
        checkGreenHopper();

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
    
    public Issue createIssueFromTask(Task task) throws JiraException {
        return jira.createIssue(task.get("project"), task.get("type"))
                .field("summary", task.get("summary"))
                .field("description", task.get("description"))
                .execute();
    }
    
    private void printWithCheck(PrintStream out, String key, String value) {
        if (value != null) {
            out.println(key + ": " + value);
        }
    }
    
    public void printYaml(SprintIssue issue, PrintStream out) {
        printWithCheck(out, "key", issue.getKey());
        printWithCheck(out, "summary", issue.getSummary());
        out.println("---");
    }
    
    public void print(SprintIssue issue, PrintStream out) {
        out.println("[" + issue.getKey() + "] " + issue.getSummary());
    }

    public void printYaml(Collection<SprintIssue> issues, PrintStream out) {
        for (SprintIssue issue : issues) {
            printWithCheck(out, "key", issue.getKey());
            printWithCheck(out, "summary", issue.getSummary());
            out.println("---");
        }
    }

    public void print(Collection<SprintIssue> issues, PrintStream out) {
        for (SprintIssue issue : issues) {
            System.out.println("[" + issue.getKey() + "] " + issue.getSummary());
        }
    }

}
