package cz.zoom.whiteboard.cmdline;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public abstract class Command {
    public static final PrintStream nullPrintStream = new PrintStream(new NullOutputStream());
    protected PrintStream out = nullPrintStream;

    public static class NullOutputStream extends OutputStream {

        @Override
        public void write(int i) throws IOException {
        }
    
    }
    
    public abstract void run(CommandLine commandLine, String[] arguments) throws CommandException;

    public void registerOutput(PrintStream out) {
        this.out = out;
    }
    
    public PrintStream getOutputStream() {
        return System.out;
    }
    
}
