package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.cmdline.CommandLineParser.Option;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandLineComposite extends Command {
    private final Map<String, Command> commands = new HashMap<String, Command>();
    private final Set<PrintStream> outputStreams = new HashSet<PrintStream>();
    private final PrintStream compositeOutput = new PrintStream(new CompositeDataSink());

    private class CompositeDataSink extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            for (PrintStream stream : outputStreams) {
                stream.write(b);
            }
        }
        
    }
    
    public void registerCommand(String name, Command command) {
        command.registerOutput(compositeOutput);
        commands.put(name, command);
        registerOutput(command.getOutputStream());
    }

    public void destroy() {
        commands.clear();
        outputStreams.clear();
    }

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        for (Option option : commandLine) {
            String name = option.getName();

            if (commands.containsKey(name)) {
                commands.get(name).run(commandLine, option.getArguments());
            }
        }
    }
    
    @Override
    public void registerOutput(PrintStream out) {
        outputStreams.add(out);
    }

    public void unregisterOutput(PrintStream out) {
        outputStreams.remove(out);
    }
}
