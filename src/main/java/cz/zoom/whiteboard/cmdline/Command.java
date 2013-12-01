package cz.zoom.whiteboard.cmdline;

import java.io.PrintStream;

public abstract class Command {
    protected PrintStream out = System.out;

    public abstract void run(CommandLine commandLine, String[] arguments) throws CommandException;

    public void registerOutput(PrintStream out) {
        this.out = out;
    }
    
    public PrintStream getOutputStream() {
        return System.out;
    }
    
}
