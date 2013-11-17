package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.Boundary;
import java.io.File;
import javax.imageio.ImageIO;
import net.jcip.annotations.Immutable;

@Immutable
public class Bounds extends Command {
  
    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length < 2) {
            throw new CommandException("Bounds: Two arguments needed!");
        }
        
        Boundary boundary = new Boundary(arguments[0]);

        try {
            for (int i = 0; i < 4; i++) {
                ImageIO.setUseCache(false);

                String key = arguments[1] + i;
                File file = new File(key + ".png");
                ImageIO.write(boundary.render((i+1) + "/4 " + arguments[0]), "PNG", file);
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
}
