package cz.zoom.whiteboard;

import java.util.HashMap;
import java.util.Map;

public class CommandLineParser {
    
    private static final String[][] KNOWN_OPTIONS = {
        { "encode", "e" },
        { "decode", "d" },
        { "whiteboard", "w" }
    };
    
    public final static class CommandLine {
        
        private final Map<String, String> options;
        
        public CommandLine(Map<String, String> options) {
            this.options = options;
        }
        
        public boolean has(String option) {
            return options.containsKey(option);
        }
        
        public boolean hasArgument(String option) {
            return options.get(option) != null;
        }
        
        public String getArgument(String option) {
            return options.get(option);
        }
        
        @Override
        public String toString() {
            return options.toString();
        }
        
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
