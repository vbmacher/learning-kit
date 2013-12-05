package cz.zoom.whiteboard.cmdline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandLineParser {
    public static final String OPT_DECODE = "decode";
    public static final String OPT_GROUP = "group";
    public static final String OPT_FIND = "find";
    
    public static final String OPT_CREATE = "create";
    public static final String OPT_UPDATE = "update";
    public static final String OPT_OPENBYKEY = "openByKey";
    public static final String OPT_OPENBYID = "openByID";
    
    public static final String OPT_YAML = "yaml";
    public static final String OPT_RENDER = "render";
    public static final String OPT_CHECK = "check";
    public static final String OPT_RENDER_EMPTY = "empty";
    
    public static final String OPT_URL = "url";
    public static final String OPT_LOGIN = "login";
    public static final String OPT_PASSWORD = "password";
    
    public static final String OPT_WHITEBOARD = "whiteboard";
    
    private static final String[][] KNOWN_OPTIONS = {
        { OPT_DECODE, "d", "pngFile", "Decode PNG image into YAML file" },
        { OPT_GROUP, "g", "groupName pngFileBase", "Render a named group - 4 image files. Name should be in YAML format" },
        { OPT_FIND, "f", "groupName pngFile", "Find text content inside a named group found in specified image file" },
        
        { OPT_CREATE, "c", "yamlFile", "Create JIRA issues from a YAML file" },
        { OPT_UPDATE, "U", "[yamlFile]", "Update JIRA issues from YAML (either -y is required or [yamlFile] must be specified" },
        { OPT_OPENBYKEY, "o", "issueKey", "List all open issues of a sprint based on issue key"},
        { OPT_OPENBYID, "O", "rapidViewID sprintID", "List all open issues of a sprint based on rapidViewID and sprintID"},
        
        { OPT_YAML, "y", null, "Use strict YAML output (relevant only for -o,-O,-d,-f)" },
        { OPT_RENDER, "r", null, "Render YAML into PNG output (-y is required)" },
        { OPT_CHECK, "C", null, "Check if fields in JIRA equal to those in an YAML (-y is required)" },
        { OPT_RENDER_EMPTY, "e", null, "If -r option is enabled, render PNG without any printed text" },
        
        { OPT_URL, "u", "jiraURL", "Set JIRA URL (optional)"},
        { OPT_LOGIN, "l", "userName", "Set user name used for JIRA authentication (optional)"},
        { OPT_PASSWORD, "p", "password", "Set password used for JIRA authentication (optional)"},

        { OPT_WHITEBOARD, "w", null, "Display whiteboard GUI (experimental)" },
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
            usage.append("\n  ").append(option[OPTION_DESCRIPTION]);
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
