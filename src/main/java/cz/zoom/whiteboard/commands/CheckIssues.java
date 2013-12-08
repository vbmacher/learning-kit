package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.ConnectionDetails;
import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.StringDataSink;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import cz.zoom.whiteboard.task.Tasks;
import cz.zoom.whiteboard.task.TasksFactory;
import java.io.PrintStream;

public class CheckIssues extends Command {
    private final StringDataSink dataSink = new StringDataSink();
    
    @Override
    public PrintStream getOutputStream() {
        return new PrintStream(dataSink);
    }

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (!commandLine.hasOption(CommandLineParser.OPT_YAML)) {
            throw new CommandException("YAML output option must be set!");
        }

        try {
            Tasks tasks = TasksFactory.createFromYamlText(dataSink.getStringData());
            
            ConnectionDetails details = new ConnectionDetails(
                    commandLine.getFirstArgument(CommandLineParser.OPT_LOGIN),
                    commandLine.getFirstArgument(CommandLineParser.OPT_PASSWORD),
                    commandLine.getFirstArgument(CommandLineParser.OPT_URL));
            
            JiraAdapter jira = new JiraAdapter(details, out);
            jira.checkIssues(tasks);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
}
