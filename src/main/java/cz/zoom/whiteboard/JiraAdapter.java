package cz.zoom.whiteboard;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Issue;
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
    
    public List<SprintIssue> getSprintIssues() throws JiraException {
        if (greenHopper == null) {
            throw new UnsupportedOperationException("GreenHopper JIRA plugin is not accessible");
        }
        
        List<SprintIssue> issues = new ArrayList<SprintIssue>();

        for (RapidView view : greenHopper.getRapidViews()) {
            for (Sprint sprint : view.getSprints()) {
                if (!sprint.isClosed()) {
                    System.out.println("Sprint: id=" + sprint.getId() +"; " + sprint.getName());
                    SprintReport report = view.getSprintReport(sprint);
                    issues.addAll(report.getCompletedIssues());
                    issues.addAll(report.getIncompletedIssues());
                    issues.addAll(report.getPuntedIssues());
                }
            }
        }
        return issues;
    }
    
    public Issue createIssueFromTask(Task task) throws JiraException {
        return jira.createIssue(task.getProject(), task.getType())
                .field("summary", task.getSummary())
                .field("description", task.getDescription())
                .execute();
        
    }
    

}
