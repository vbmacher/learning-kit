package cz.zoom.whiteboard;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicStatus;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueType;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class JiraIssue {
    public static final String STATE_READY_FOR_ACCEPTACNE = "Ready For Acceptance";
    public static final String STATE_CLOSED = "Closed";
    public static final String STATE_IN_PROGRESS = "In Progress";
    
    public static final String TYPE_TECHNICAL_TASK = "Technical Task";
    public static final String TYPE_BUG = "Bug";

    private final JerseyJiraRestClientFactory restClientFactory = new JerseyJiraRestClientFactory();
    private final NullProgressMonitor progressMonitor = new NullProgressMonitor();
    private final JiraRestClient restClient;
    public final Map<String, Long> issueTypes = new HashMap<String, Long>();
    
    public JiraIssue(String jiraApi, String userName, String password) throws URISyntaxException {
        this.restClient = restClientFactory.createWithBasicHttpAuthentication(
                new URI(jiraApi), userName, password);
        for (IssueType type : restClient.getMetadataClient().getIssueTypes(progressMonitor)) {
            issueTypes.put(type.getName(), type.getId());
        }
    }

    public boolean hasState(String issueName, String state) {
        Issue issue = restClient.getIssueClient().getIssue(issueName, progressMonitor);
        
        if (issue == null) {
            return false;
        }
        String[] states = state.split(",");
        BasicStatus issueStatus = issue.getStatus();
        for (String stateName : states) {
            if (issueStatus.getName().equals(stateName.trim())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean exists(String summary) {
        SearchResult result = restClient.getSearchClient().searchJql("summary = \"" + summary + "\"", progressMonitor);
        return result.getTotal() > 0;
    }
    
    private IssueInput prepareIssueInput(Task task) {
        IssueInputBuilder issueBuilder = new IssueInputBuilder(
                task.getProject(),
                issueTypes.get(TYPE_TECHNICAL_TASK));
        issueBuilder.setDescription(task.getData().get("description"));
        issueBuilder.setSummary(task.getSummary());
        
        // TODO: add other stuff defined by rules
        return issueBuilder.build();
    }
    
    public BasicIssue create(Task task) {
        String id = task.getId();
        if (id != null) {
            return null;
        }
        String summary = task.getSummary();
        if ((summary == null) || exists(summary)) {
            return null;
        }
        return restClient.getIssueClient().createIssue(prepareIssueInput(task), progressMonitor);
    }
}
