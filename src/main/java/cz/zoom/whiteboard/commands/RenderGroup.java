package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.Group;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import java.io.File;
import javax.imageio.ImageIO;
import net.jcip.annotations.Immutable;

@Immutable
public class RenderGroup extends Command {
  
    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length < 2) {
            throw new CommandException("RenderGroup: Two arguments needed!");
        }
        
        Group namespace = new Group(arguments[0]);
        try {
            for (int i = 0; i < 4; i++) {
                ImageIO.setUseCache(false);

                String key = arguments[1] + i;
                File file = new File(key + ".png");
                ImageIO.write(namespace.render((i+1) + "/4 " + arguments[0]), "PNG", file);
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
}
