package cz.zoom.whiteboard.cmdline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class CommandLine {
    private final List<CommandLineParser.Option> options = new ArrayList<CommandLineParser.Option>();

    public CommandLine(Map<String, String> options) {
        for (final Map.Entry<String, String> option : options.entrySet()) {
            this.options.add(new CommandLineParser.Option() {
                private final String argument = option.getValue();
                private final String name = option.getKey();

                public String getName() {
                    return name;
                }

                public boolean hasArgument() {
                    return argument != null;
                }

                public String getArgument() {
                    return argument;
                }
            });
        }
    }

    public CommandLineParser.Option getOption(String optionName) {
        for (CommandLineParser.Option option : options) {
            if (option.getName().equals(optionName)) {
                return option;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return options.isEmpty();
    }

    public Iterator<CommandLineParser.Option> iterator() {
        return options.iterator();
    }

    @Override
    public String toString() {
        return options.toString();
    }
    
}
