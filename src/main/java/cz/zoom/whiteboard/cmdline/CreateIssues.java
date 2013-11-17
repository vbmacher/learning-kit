package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.cmdline.Command;

public class CreateIssues extends Command {

    private static class Specification {
        public final String storyKey;
        public final String yamlFile;
        
        public final String userName;
        public final String password;
        public final String url;

        public Specification(String storyKey, String yamlFile, String userName, String password, String url) {
            this.storyKey = storyKey;
            this.yamlFile = yamlFile;
            this.userName = userName;
            this.password = password;
            this.url = url;
        }
        
    }
    

    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        String storyKey = arguments[0];
        String yamlFile = arguments[1];
        
        
    }
    
}
