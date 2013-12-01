package cz.zoom.whiteboard.commands;

import cz.zoom.whiteboard.Group;
import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import cz.zoom.whiteboard.task.TaskFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class FindGroup extends Command {

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length < 2) {
            throw new CommandException("FindGroup: Two arguments are needed!");
        }
        boolean yamlOutput = commandLine.hasOption(CommandLineParser.OPT_YAML);
        
        try {
            Group boundary = new Group(arguments[0]);

            BufferedImage image = ImageIO.read(new File(arguments[1]));
            String[] issues = boundary.decode(image);
            
            if (!yamlOutput) {
                out.println("Recognized " + issues.length + " issues in the specified boundary.\n");
            }
            for (String issue : issues) {
                if (yamlOutput) {
                    out.println("{ " + JiraAdapter.ISSUE_KEY + ": "
                            + TaskFactory.createFromYamlText(issue).get(JiraAdapter.ISSUE_KEY)
                            + ", " + arguments[0]
                            + "}");
                    out.println("---");
                } else {
                    out.println(issue);
                }
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }

    }
    
}
