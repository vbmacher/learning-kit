package cz.zoom.whiteboard.cmdline;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommandLineMediator {
    
    private final CommandLine commandLine;
    private final Map<String, Command> commands = new HashMap<String, Command>();
    
    public interface Command {
        
        public void run(CommandLine commandLine, String argument) throws CommandException;
        
    }

    public CommandLineMediator(CommandLine commandLine) {
        this.commandLine = commandLine;
    }
    
    public void registerCommand(String name, Command command) {
        commands.put(name, command);
    }
    
    public void destroy() {
        commands.clear();
    }

    public void processCommandLine() throws CommandException {
        Iterator<CommandLineParser.Option> iterator = commandLine.iterator();
        while (iterator.hasNext()) {
            CommandLineParser.Option option = iterator.next();
            String name = option.getName();

            if (commands.containsKey(name)) {
                commands.get(name).run(commandLine, option.getArgument());
            } else {
                throw new CommandException("Unknown option: " + name);
            }
        }
   }
}
