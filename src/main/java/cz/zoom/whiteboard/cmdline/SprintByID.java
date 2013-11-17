package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.JiraAdapter;
import cz.zoom.whiteboard.cmdline.Command;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.SprintIssue;

public class SprintByID extends Command {
    
    private static class Specification {
        public final String userName;
        public final String password;
        public final int rapidViewID;
        public final int sprintID;
        public final String URL;
        public final boolean yamlOutput;
        
        public Specification(String url,
                String userName, String password, int rapidViewID, int spritnID,
                boolean yamlOutput) {
            this.URL = url;
            this.userName = userName;
            this.password = password;
            this.rapidViewID = rapidViewID;
            this.sprintID = spritnID;
            this.yamlOutput = yamlOutput;
        }
        
    }

    @Override
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length < 2) {
            throw new CommandException("SprintByID: Two arguments needed!");
        }

        String login = commandLine.getFirstArgument("login");
        String password = commandLine.getFirstArgument("password");
        String url = commandLine.getFirstArgument("url");

        int rapidViewID = Integer.parseInt(arguments[0]);
        int sprintID = Integer.parseInt(arguments[1]);
        boolean yamlOutput = commandLine.hasOption("yaml");

        try {
            findTasks(new Specification(
                    url,
                    login,
                    password,
                    rapidViewID,
                    sprintID,
                    yamlOutput));
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    private void findTasks(Specification spec) throws URISyntaxException, JiraException, IOException {
        JiraAdapter jira = new JiraAdapter(spec.URL, spec.userName, spec.password, true);
        List<SprintIssue> issues = jira.getOpenSprintIssues(spec.rapidViewID, spec.sprintID);
        if (issues == null) {
            if (!spec.yamlOutput) {
                System.out.println("No issues found");
            }
        } else {
            if (spec.yamlOutput) {
                jira.printYaml(issues, out);
            } else {
                jira.print(issues, out);
            }
        }
    }
    
}
