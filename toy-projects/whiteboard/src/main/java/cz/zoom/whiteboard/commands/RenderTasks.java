package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.StringDataSink;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import cz.zoom.whiteboard.task.Task;
import cz.zoom.whiteboard.task.Tasks;
import cz.zoom.whiteboard.task.TasksFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class RenderTasks extends Command {
    private final StringDataSink dataSink = new StringDataSink();

    @Override
    public PrintStream getOutputStream() {
        return new PrintStream(dataSink);
    }

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length == 0 && !commandLine.hasOption(CommandLineParser.OPT_YAML)) {
            throw new CommandException("YAML output or file argument must be set!");
        }

        Integer width = null;
        Integer height = null;
        if (commandLine.hasOption(CommandLineParser.OPT_RENDER_SIZE)) {
            if (!commandLine.hasOption(CommandLineParser.OPT_RENDER_EMPTY)) {
                throw new CommandException("Render empty option must be set!");
            }
            width = Integer.parseInt(commandLine.getArgument(CommandLineParser.OPT_RENDER_SIZE, 0));
            height = Integer.parseInt(commandLine.getArgument(CommandLineParser.OPT_RENDER_SIZE, 1));
        }

        boolean renderEmpty = commandLine.hasOption(CommandLineParser.OPT_RENDER_EMPTY);
        try {
            Tasks tasks;
            if (arguments.length > 0) {
                tasks = TasksFactory.createFromYamlFile(arguments[0]);
            } else {
                tasks = TasksFactory.createFromYamlText(dataSink.getStringData());
            }

            int i = 0;
            ImageIO.setUseCache(false);

            System.out.println("Tasks count: " + tasks.size());
            for (Task task : tasks) {
                String key = task.get(JiraAdapter.ISSUE_KEY);
                if (key == null) {
                    key = "task-" + (i++);
                }

                BufferedImage renderedTask;
                renderedTask = (width != null && height != null)
                        ? task.render(width, height, renderEmpty)
                        : task.render(renderEmpty);

                File file = new File(key + ".png");
                ImageIO.write(renderedTask, "PNG", file);
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

}
