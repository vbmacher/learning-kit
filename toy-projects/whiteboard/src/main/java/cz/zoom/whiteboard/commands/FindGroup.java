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
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;

public class FindGroup extends Command {

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length == 0) {
            throw new CommandException("FindGroup: At least one argument is needed (PNG file)!");
        }
        boolean yamlOutput = commandLine.hasOption(CommandLineParser.OPT_YAML);
        
        try {
            BufferedImage image = ImageIO.read(new File(arguments[0]));

            Set<Group> groups;
            if (arguments.length == 1) {
                groups = Group.findAll(image);
            } else {
                groups = new HashSet<Group>();
                
                for (int i = 1; i < arguments.length; i++) {
                    groups.add(new Group(arguments[i]));
                }
            }

            for (Group group : groups) {
                String[] issues = group.decode(image);

                if (!yamlOutput) {
                    out.println("Recognized " + issues.length + " issues in group " + group + "\n");
                }
                for (String issue : issues) {
                    if (yamlOutput) {
                        out.println("{ " + JiraAdapter.ISSUE_KEY + ": "
                                + TaskFactory.createFromYamlText(issue).get(JiraAdapter.ISSUE_KEY)
                                + ", " + group.getName()
                                + "}");
                        out.println("---");
                    } else {
                        out.println(issue);
                    }
                }
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }

    }
    
}
