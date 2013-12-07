package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.ConnectionDetails;
import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.StringDataSink;
import cz.zoom.whiteboard.task.Tasks;
import cz.zoom.whiteboard.task.TasksFactory;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import java.io.PrintStream;

public class UpdateIssues extends Command {
    private final StringDataSink dataSink = new StringDataSink();

    @Override
    public PrintStream getOutputStream() {
        return new PrintStream(dataSink);
    }

    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length == 0 && !commandLine.hasOption(CommandLineParser.OPT_YAML)) {
            throw new CommandException("YAML output or file argument must be set!");
        }
        
        ConnectionDetails details = new ConnectionDetails(
                commandLine.getFirstArgument(CommandLineParser.OPT_LOGIN),
                commandLine.getFirstArgument(CommandLineParser.OPT_PASSWORD),
                commandLine.getFirstArgument(CommandLineParser.OPT_URL));
        
        try {
            Tasks tasks;
            if (arguments.length > 0) {
                tasks = TasksFactory.createFromYamlFile(arguments[0]);
            } else {
                tasks = TasksFactory.createFromYamlText(dataSink.getStringData());
            }

            JiraAdapter jira = new JiraAdapter(details);
            jira.updateIssues(tasks);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
}
