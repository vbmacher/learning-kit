package cz.zoom.whiteboard;

import cz.zoom.whiteboard.cmdline.Bounds;
import cz.zoom.whiteboard.cmdline.LaunchWhiteboard;
import cz.zoom.whiteboard.cmdline.DecodePNG;
import cz.zoom.whiteboard.cmdline.Render;
import cz.zoom.whiteboard.cmdline.SprintByIssue;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineComposite;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import cz.zoom.whiteboard.cmdline.CreateIssues;
import cz.zoom.whiteboard.cmdline.Locate;
import cz.zoom.whiteboard.cmdline.SprintByID;
import java.io.PrintStream;

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
        
        CommandLineComposite composite = new CommandLineComposite();
        
        Render render = new Render();
        composite.registerCommand("render", render);
        composite.registerCommand("decode", new DecodePNG());
        composite.registerCommand("whiteboard", new LaunchWhiteboard());
        composite.registerCommand("issue", new SprintByIssue());
        composite.registerCommand("sprint", new SprintByID());
        composite.registerCommand("create", new CreateIssues());
        composite.registerCommand("bounds", new Bounds());
        composite.registerCommand("locate", new Locate());

        try {
            if (cmdLine.hasOption("render")) {
                composite.setOutPrintStream(new PrintStream(render.getOutputStream()));
            }
            composite.run(cmdLine, null);
        } finally {
            composite.destroy();
        }
    }

}
