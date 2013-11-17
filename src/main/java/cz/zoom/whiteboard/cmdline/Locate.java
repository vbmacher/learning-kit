package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.Boundary;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Locate extends Command {

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length < 2) {
            throw new CommandException("Locate: Two arguments are needed!");
        }
        boolean yamlOutput = commandLine.hasOption("yaml");
        
        try {
            Boundary boundary = new Boundary(arguments[0]);

            BufferedImage image = ImageIO.read(new File(arguments[1]));
            String[] issues = boundary.decodeIssues(image);
            
            if (!yamlOutput) {
                out.println("Recognized " + issues.length + " issues in the specified boundary.\n");
            }
            for (String issue : issues) {
                out.println(issue);
                if (yamlOutput) {
                    out.println("---");
                }
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }

    }
    
}
