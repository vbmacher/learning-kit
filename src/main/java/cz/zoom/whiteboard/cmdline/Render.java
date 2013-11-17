package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.Task;
import cz.zoom.whiteboard.Tasks;
import cz.zoom.whiteboard.TasksFactory;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class Render extends Command {
    private final YamlDataSink dataSink = new YamlDataSink();
    
    @NotThreadSafe
    private static class YamlDataSink extends OutputStream {
        String data = "";

        @Override
        public void write(int i) throws IOException {
            data += (char)i;
        }
        
        public String getData() {
            return data;
        }
    }
    
    public OutputStream getOutputStream() {
        return dataSink;    
    }
    
    public void run(CommandLine commandLine, String[] argument) throws CommandException {
        if (!commandLine.hasOption("yaml")) {
            throw new CommandException("YAML output option must be set!");
        }
        try {
            Tasks tasks = TasksFactory.createFromYamlText(dataSink.getData());
            int i = 0;
            ImageIO.setUseCache(false);
            
            for (Task task : tasks) {
                String key = task.get("key");
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
