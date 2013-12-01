package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.task.Task;
import cz.zoom.whiteboard.task.Tasks;
import cz.zoom.whiteboard.task.TasksFactory;
import cz.zoom.whiteboard.StringDataSink;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
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
    
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (!commandLine.hasOption(CommandLineParser.OPT_YAML)) {
            throw new CommandException("YAML output option must be set!");
        }
        try {
            Tasks tasks = TasksFactory.createFromYamlText(dataSink.getStringData());
            int i = 0;
            ImageIO.setUseCache(false);
            
            for (Task task : tasks) {
                String key = task.get(JiraAdapter.ISSUE_KEY);
                if (key == null) {
                    key = "task-" + (i++);
                }
                
                File file = new File(key + ".png");
                ImageIO.write(task.render(), "PNG", file);
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
}
