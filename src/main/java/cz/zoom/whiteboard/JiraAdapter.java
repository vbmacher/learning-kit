package cz.zoom.whiteboard;

import java.net.URISyntaxException;
import java.util.ArrayList;
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
    
    public JiraAdapter(String jiraURL, String userName, String password, boolean useGreenHopper) throws URISyntaxException {
        credentials = new BasicCredentials(userName, password);
        jira = new JiraClient(jiraURL, credentials);
        if (useGreenHopper) {
            greenHopper = new GreenHopperClient(jira);
        } else {
            greenHopper = null;
        }
    }
    
    public Issue getIssue(String issueKey) throws JiraException {
        return jira.getIssue(issueKey);
    }
    
    private boolean containsIssue(List<Issue> issues, String issueKey) throws JiraException {
        for (Issue issue : issues) {
            if (issue.getKey().equals(issueKey)) {
                return true;
            } else {
                SearchResult result = jira.searchIssues("parent=" + issue.getKey());
                if (containsIssue(result.issues, issueKey)) {
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
    
    private List<SprintIssue> getOpenIssues(SprintReport report, String issueKey) throws JiraException {
        List<SprintIssue> issues = new ArrayList<SprintIssue>();
        boolean found
                = containsIssue(jiraIssues(report.getCompletedIssues()), issueKey)
                || containsIssue(jiraIssues(report.getIncompletedIssues()), issueKey)
                || containsIssue(jiraIssues(report.getPuntedIssues()), issueKey);

        if (found) {
            for (SprintIssue issue : report.getIncompletedIssues()) {
                if (!issue.isDone()) {
                    //SearchResult result = jira.searchIssues("parent=" + issueKey);
                    issues.add(issue);
                }
            }
            return issues;
        }
        return issues;
    }
    
    public List<SprintIssue> getOpenSprintIssues(String issueKey) throws JiraException {
        if (greenHopper == null) {
            throw new UnsupportedOperationException("GreenHopper JIRA plugin is not accessible");
        }

        Set<Integer> sprintIds = new HashSet<Integer>();
        for (RapidView view : greenHopper.getRapidViews()) {
            for (Sprint sprint : view.getSprints()) {
                if (sprintIds.contains(sprint.getId())) {
                    continue;
                }
                sprintIds.add(sprint.getId());
                try {
                    SprintReport report = view.getSprintReport(sprint);
                    System.out.println("sprint: id=" + sprint.getId() + "; " + sprint.getName());
                    List<SprintIssue> issues = getOpenIssues(report, issueKey);
                    if (!issues.isEmpty()) {
                        return issues;
                    }

                } catch (JiraException e) {
                }
            }
        }
        return null;
    }
    
    public Issue createIssueFromTask(Task task) throws JiraException {
        return jira.createIssue(task.getProject(), task.getType())
                .field("summary", task.getSummary())
                .field("description", task.getDescription())
                .execute();
        
    }
    

}
