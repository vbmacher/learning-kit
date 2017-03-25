package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.cmdline.CommandLineParser.Option;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class CommandLine implements Iterable<Option> {
    private final List<Option> options = new ArrayList<Option>();

    public CommandLine(Collection<Option> options) {
        this.options.addAll(options);
    }
    
    public boolean hasOption(String optionName) {
        for (Option option : options) {
            if (option.getName().equals(optionName)) {
                return true;
            }
        }
        return false;
    }

    public Option getOption(String optionName) {
        for (Option option : options) {
            if (option.getName().equals(optionName)) {
                return option;
            }
        }
        return null;
    }
    
    public String getFirstArgument(String optionName) throws CommandException {
        return getArgument(optionName, 0);
    }
    
    public String getArgument(String optionName, int index) throws CommandException {
        Option option = getOption(optionName);
        if (option == null) {
            return null;
        }
        if (!option.hasArguments()) {
            throw new CommandException(optionName + " must have argument!");
        }
        return option.getArguments()[index];
    }

    public boolean isEmpty() {
        return options.isEmpty();
    }

    public Iterator<Option> iterator() {
        return options.iterator();
    }

    @Override
    public String toString() {
        return options.toString();
    }
    
}
