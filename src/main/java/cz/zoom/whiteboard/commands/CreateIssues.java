package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.ConnectionDetails;
import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import cz.zoom.whiteboard.task.Tasks;
import cz.zoom.whiteboard.task.TasksFactory;

public class CreateIssues extends Command {

    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length == 0) {
            throw new CommandException("CreateIssues: Argument must be specified!");
        }

        ConnectionDetails details = new ConnectionDetails(
                commandLine.getFirstArgument(CommandLineParser.OPT_LOGIN),
                commandLine.getFirstArgument(CommandLineParser.OPT_PASSWORD),
                commandLine.getFirstArgument(CommandLineParser.OPT_URL));

        try {
            Tasks tasks = TasksFactory.createFromYamlFile(arguments[0]);

            JiraAdapter jira = new JiraAdapter(details);
            jira.createIssues(tasks);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

}
