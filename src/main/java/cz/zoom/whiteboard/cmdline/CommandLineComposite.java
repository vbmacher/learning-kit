package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.cmdline.CommandLineParser.Option;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommandLineComposite extends Command {
    private final Map<String, Command> commands = new HashMap<String, Command>();

    public void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    public void destroy() {
        commands.clear();
    }

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        Iterator<Option> iterator = commandLine.iterator();
        while (iterator.hasNext()) {
            Option option = iterator.next();
            String name = option.getName();

            if (commands.containsKey(name)) {
                commands.get(name).run(commandLine, option.getArguments());
            }
        }
    }
    
    @Override
    public void setOutPrintStream(PrintStream out) {
        for (Command command : commands.values()) {
            command.setOutPrintStream(out);
        }
    }

}
