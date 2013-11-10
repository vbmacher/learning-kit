package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.Tasks;
import cz.zoom.whiteboard.TasksFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class RenderYAML implements CommandLineMediator.Command {

    public void run(CommandLine commandLine, String fileName) throws CommandException {
        try {
            Tasks generator = TasksFactory.createFromYamlFile(fileName);
            int i = 0;
            ImageIO.setUseCache(false);
            for (BufferedImage taskImage : generator.render()) {
                File file = new File("task-" + i + ".png");
                ImageIO.write(taskImage, "PNG", file);
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
}
