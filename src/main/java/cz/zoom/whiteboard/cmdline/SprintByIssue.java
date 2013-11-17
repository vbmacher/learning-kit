package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.JiraAdapter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.SprintIssue;

public class SprintByIssue extends Command {
    
    private static class Specification {
        public final String userName;
        private final String password;
        private final String url;
        private final String issueKey;
        public final boolean yamlOutput;

        public Specification(String userName, String password, String url, String issueKey, boolean yamlOutput) {
            this.userName = userName;
            this.password = password;
            this.url = url;
            this.issueKey = issueKey;
            this.yamlOutput = yamlOutput; 
        }
        
    }
    
    @Override
    public void run(CommandLine commandLine, String[] issues) throws CommandException {
        if (issues.length < 1) {
            throw new CommandException("SprintByIssue: One or more arguments needed!");
        }

        String login = commandLine.getFirstArgument("login");
        String password = commandLine.getFirstArgument("password");
        String url = commandLine.getFirstArgument("url");

        boolean yamlOutput = commandLine.hasOption("yaml");
        for (String issue : issues) {
            try {
                findTasks(new Specification(login, password, url, issue, yamlOutput));
            } catch (Exception e) {
                throw new CommandException(e);
            }
        }
    }

    private void findTasks(Specification spec) throws URISyntaxException, JiraException, IOException {
        JiraAdapter jira = new JiraAdapter(spec.url, spec.userName, spec.password, true);
        List<SprintIssue> issues = jira.getOpenSprintIssues(spec.issueKey);
        if (issues == null) {
            if (!spec.yamlOutput) {
                out.println("No issues found");
            }
        } else {
            if (spec.yamlOutput) {
                jira.printYaml(issues, out);
            } else {
                jira.print(issues, out);
            }
        }
    }
        
}
