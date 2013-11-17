package cz.zoom.whiteboard.cmdline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandLineParser {
    
    private static final String[][] KNOWN_OPTIONS = {
        { "decode", "d", "pngFile", "Decode PNG image into YAML file" },
        { "create", "c", "yamlFile", "Create/update JIRA issues from YAML file" },
        { "issue", "i", "issueKey", "Find all open issues from sprint based on issue key"},
        { "sprint", "s", "rapidViewID sprintID", "Find all open issues from sprint based on issue key"},
        { "bounds", "b", "yamlFields pngFile", "Render 4 PNG boundaries with specified JIRA issue fields" },
        { "locate", "L", "yamlFields pngFile", "Locate 4 PNG boundaries with specified JIRA issue fields" },
        
        { "yaml", "y", null, "YAML output (relevant only for -s,-i,-d,-L)" },
        { "render", "r", null, "PNG output (-y is required)" },
        
        { "url", "u", "jiraURL", "Set JIRA URL (optional)"},
        { "login", "l", "userName", "User name used in JIRA authentication (optional)"},
        { "password", "p", "password", "Password used in JIRA authentication (optional)"},

        { "whiteboard", "w", null, "Display whiteboard GUI (experimental)" },
    };
    
    private static final int OPTION_LONG_NAME = 0;
    private static final int OPTION_SHORT_NAME = 1;
    private static final int OPTION_ARGUMENTS = 2;
    private static final int OPTION_DESCRIPTION = 3;
    
    public interface Option {
        public String getName();
        public boolean hasArguments();
        int getArgumentsCount();
        public String[] getArguments();
    }
    
    private static class OptionImpl implements Option {
        private final String name;
        private final Collection<String> arguments; 

        public OptionImpl(String name, Collection<String> arguments) {
            this.name = name;
            this.arguments = arguments;
        }

        public String getName() {
            return name;
        }

        public boolean hasArguments() {
            return !arguments.isEmpty();
        }

        public int getArgumentsCount() {
            return arguments.size();
        }

        public String[] getArguments() {
            return arguments.toArray(new String[0]);
        }
    }
    
    public static void usage() {
        StringBuilder usage = new StringBuilder("Usage: java -jar whiteboard.jar");
        for (String[] option : KNOWN_OPTIONS) {
            usage.append(" -").append(option[OPTION_SHORT_NAME]);
        }
        usage.append("\n");
        
        for (String[] option : KNOWN_OPTIONS) {
            usage.append("\n-").append(option[OPTION_SHORT_NAME]);
            
            String arguments = option[OPTION_ARGUMENTS];
            
            usage.append(" [ --").append(option[OPTION_LONG_NAME]).append("\t]");
            if (arguments != null) {
                usage.append(" <").append(arguments).append("> ");
            } else {
                usage.append("\t");
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
        List<Option> options = new ArrayList<Option>();

        String lastOption = null;
        List<String> lastArgs = new ArrayList<String>();
        for (String argument : args) {
            String option = findKnownOption(argument);
            
            if ((option == null) && (lastOption != null)) {
                lastArgs.add(argument);
            } else if ((option != null) && (lastOption != null)) {
                options.add(new OptionImpl(lastOption, new ArrayList<String>(lastArgs)));
                lastArgs.clear();
                lastOption = option;
            } else if (lastOption == null) {
                lastOption = option;
                lastArgs.clear();
            }
        }
        if (lastOption != null) {
            options.add(new OptionImpl(lastOption, lastArgs));
        }
        
        return new CommandLine(options);
    }
    
    
}
