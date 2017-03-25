package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.ConnectionDetails;
import cz.zoom.whiteboard.JiraAdapter;import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.SprintIssue;

public class OpenIssuesByID extends Command {
    
    private static class Specification {
        public final int rapidViewID;
        public final int sprintID;
        public final boolean yamlOutput;
        
        public Specification(int rapidViewID, int spritnID, boolean yamlOutput) {
            this.rapidViewID = rapidViewID;
            this.sprintID = spritnID;
            this.yamlOutput = yamlOutput;
        }
    }

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length < 2) {
            throw new CommandException("OpenIssuesByID: Two arguments needed!");
        }

        ConnectionDetails details = new ConnectionDetails(
                commandLine.getFirstArgument(CommandLineParser.OPT_LOGIN),
                commandLine.getFirstArgument(CommandLineParser.OPT_PASSWORD),
                commandLine.getFirstArgument(CommandLineParser.OPT_URL));

        int rapidViewID = Integer.parseInt(arguments[0]);
        int sprintID = Integer.parseInt(arguments[1]);
        boolean yamlOutput = commandLine.hasOption(CommandLineParser.OPT_YAML);

        try {
            findTasks(details, new Specification(rapidViewID, sprintID, yamlOutput));
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    private void findTasks(ConnectionDetails details, Specification spec) throws URISyntaxException, JiraException, IOException {
        JiraAdapter jira = new JiraAdapter(details, out);
        List<SprintIssue> issues = jira.getOpenSprintIssues(spec.rapidViewID, spec.sprintID);
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
