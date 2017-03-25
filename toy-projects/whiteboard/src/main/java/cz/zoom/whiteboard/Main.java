package cz.zoom.whiteboard;

import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineComposite;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import cz.zoom.whiteboard.commands.CheckIssues;
import cz.zoom.whiteboard.commands.CreateIssues;
import cz.zoom.whiteboard.commands.DecodeImage;
import cz.zoom.whiteboard.commands.FindGroup;
import cz.zoom.whiteboard.commands.LaunchWhiteboard;
import cz.zoom.whiteboard.commands.OpenIssuesByID;
import cz.zoom.whiteboard.commands.OpenIssuesByKey;
import cz.zoom.whiteboard.commands.RenderGroup;
import cz.zoom.whiteboard.commands.RenderTasks;
import cz.zoom.whiteboard.commands.UpdateIssues;

public class Main {

    public static void usageAndExit() {
        CommandLineParser.usage();
        System.exit(1);
    }

    public static void main( String[] args ) {
        CommandLine cmdLine = new CommandLineParser().parse(args);

        if (cmdLine.isEmpty()) {
            usageAndExit();
        }

        CommandLineComposite composite = new CommandLineComposite();

        composite.registerCommand(CommandLineParser.OPT_RENDER, new RenderTasks());
        composite.registerCommand(CommandLineParser.OPT_DECODE, new DecodeImage());
        composite.registerCommand(CommandLineParser.OPT_WHITEBOARD, new LaunchWhiteboard());
        composite.registerCommand(CommandLineParser.OPT_OPENBYKEY, new OpenIssuesByKey());
        composite.registerCommand(CommandLineParser.OPT_OPENBYID, new OpenIssuesByID());
        composite.registerCommand(CommandLineParser.OPT_CREATE, new CreateIssues());
        composite.registerCommand(CommandLineParser.OPT_UPDATE, new UpdateIssues());
        composite.registerCommand(CommandLineParser.OPT_GROUP, new RenderGroup());
        composite.registerCommand(CommandLineParser.OPT_FIND, new FindGroup());
        composite.registerCommand(CommandLineParser.OPT_CHECK, new CheckIssues());

//        if (cmdLine.hasOption(CommandLineParser.OPT_YAML)) {
//            composite.unregisterOutput(System.out);
//        }

        try {
            composite.run(cmdLine, null);
        } catch (CommandException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            usageAndExit();
        } finally {
            composite.destroy();
        }
    }

}
