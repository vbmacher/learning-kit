package cz.zoom.whiteboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommandLineParser {
    
    private static final String[][] KNOWN_OPTIONS = {
        { "render", "r", "YAML file", "Render YAML file to PNG images" },
        { "decode", "d", "PNG file", "Decode PNG image into YAML file" },
        { "url", "u", "URL", "Set JIRA URL"},
        { "login", "l", "user name", "User name used in JIRA authentication"},
        { "password", "p", "password", "Password used in JIRA authentication"},
        { "story", "s", "issue_key", "Find all tasks from user story"},
        { "whiteboard", "w", null, "Display whiteboard GUI (experimental)" },
    };
    
    private static final int OPTION_LONG_NAME = 0;
    private static final int OPTION_SHORT_NAME = 1;
    private static final int OPTION_ARGUMENTS = 2;
    private static final int OPTION_DESCRIPTION = 3;
    
    public interface Option {
        public String getName();
        public boolean hasArgument();
        public String getArgument();
    }
    
    public final static class CommandLine {
        private final List<Option> options = new ArrayList<Option>();

        public CommandLine(Map<String, String> options) {
            for (final Entry<String, String> option : options.entrySet()) {
                this.options.add(new Option() {
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
        
        public Option getOption(String optionName) {
            for (Option option : options) {
                if (option.getName().equals(optionName)) {
                    return option;
                }
            }
            return null;
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
    
    public static void usage() {
        StringBuilder usage = new StringBuilder("Usage: java -jar whiteboard.jar");
        for (String[] option : KNOWN_OPTIONS) {
            usage.append(" -").append(option[OPTION_SHORT_NAME]);
        }
        usage.append("\n");
        for (String[] option : KNOWN_OPTIONS) {
            usage.append("\n-").append(option[OPTION_SHORT_NAME])
                 .append(" [ --").append(option[OPTION_LONG_NAME]).append("\t]");
            if (option[OPTION_ARGUMENTS] != null) {
                usage.append(" <").append(option[OPTION_ARGUMENTS]).append("> ");
            }
            usage.append("\t").append(option[OPTION_DESCRIPTION]);
        }
        System.out.println(usage.toString());
    }
    
    private String findKnownOption(String argument) {
        int index;
        if (argument.startsWith("--")) {
            index = 0;
            argument = argument.substring(2);
        } else if (argument.startsWith("-")) {
            index = 1;
            argument = argument.substring(1);
        } else {
            return null;
        }
        
        for (String[] pairs : KNOWN_OPTIONS) {
            if (pairs[index].equals(argument)) {
                return pairs[0];
            }
        }
        return null;
    }
    
    public CommandLine parse(String[] args) {
        Map<String, String> options = new HashMap<String, String>();
        String lastOption = null;
        
        for (String argument : args) {
            String option = findKnownOption(argument);
            
            if (option != null) {
                options.put(option, null);
            } else if (lastOption != null) {
                options.put(lastOption, argument);
            }
            lastOption = option;
        }
        
        return new CommandLine(options);
    }
    
    
}
