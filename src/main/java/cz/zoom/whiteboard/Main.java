package cz.zoom.whiteboard;

import cz.zoom.whiteboard.cmdline.LaunchWhiteboard;
import cz.zoom.whiteboard.cmdline.DecodePNG;
import cz.zoom.whiteboard.cmdline.RenderYAML;
import cz.zoom.whiteboard.cmdline.StoryIssues;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineMediator;
import cz.zoom.whiteboard.cmdline.CommandLineParser;

public class Main {
    
    public static void usageAndExit() {
        CommandLineParser.usage();
        System.exit(1);
    }

    public static void main( String[] args ) throws CommandException {
        CommandLine cmdLine = new CommandLineParser().parse(args);
        
        if (cmdLine.isEmpty()) {
            usageAndExit();
        }
        
        CommandLineMediator mediator = new CommandLineMediator(cmdLine);
        mediator.registerCommand("render", new RenderYAML());
        mediator.registerCommand("decode", new DecodePNG());
        mediator.registerCommand("whiteboard", new LaunchWhiteboard());
        mediator.registerCommand("story", new StoryIssues());

        try {
            mediator.processCommandLine();
        } finally {
            mediator.destroy();
        }
    }

}
