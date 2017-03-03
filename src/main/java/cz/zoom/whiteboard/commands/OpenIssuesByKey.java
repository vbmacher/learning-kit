package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.ConnectionDetails;
import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.SprintIssue;

public class OpenIssuesByKey extends Command {
    
    private static class Specification {
        private final String issueKey;
        public final boolean yamlOutput;

        public Specification(String issueKey, boolean yamlOutput) {
            this.issueKey = issueKey;
            this.yamlOutput = yamlOutput; 
        }
    }
    
    @Override
    public void run(CommandLine commandLine, String[] issues) throws CommandException {
        if (issues.length < 1) {
            throw new CommandException("OpenIssuesByKey: one or more arguments needed!");
        }
        
        ConnectionDetails details = new ConnectionDetails(
                commandLine.getFirstArgument(CommandLineParser.OPT_LOGIN),
                commandLine.getFirstArgument(CommandLineParser.OPT_PASSWORD),
                commandLine.getFirstArgument(CommandLineParser.OPT_URL));
        
        boolean yamlOutput = commandLine.hasOption(CommandLineParser.OPT_YAML);
        for (String issue : issues) {
            try {
                findTasks(details, new Specification(issue, yamlOutput));
            } catch (Exception e) {
                throw new CommandException(e);
            }
        }
    }

    private void findTasks(ConnectionDetails details, Specification spec) throws URISyntaxException, JiraException, IOException {
        JiraAdapter jira = new JiraAdapter(details, out);
        List<SprintIssue> issues = jira.getOpenSprintIssues(spec.issueKey);
        if (issues == null) {
            if (!spec.yamlOutput) {
                out.println("No issues found");
            }
        } else {
            if (spec.yamlOutput) {
                jira.printYaml(issues);
            } else {
                jira.print(issues);
            }
        }
    }
        
}
