package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.IOUtils;
import cz.zoom.whiteboard.JiraAdapter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.SprintIssue;

public class StoryIssues implements CommandLineMediator.Command {
    
    private String readUserName() throws IOException {
        System.out.print("Enter user name: ");
        return IOUtils.readString();
    }
    
    private String readPassword() throws IOException {
        System.out.print("Enter password: ");
        return IOUtils.readStringSilently();
    }
    
    private String readURL() throws IOException {
        System.out.print("Enter JIRA URL: ");
        return IOUtils.readString();
    }

    private String getUserName(CommandLine commandLine) throws CommandException {
        CommandLineParser.Option optionLogin = commandLine.getOption("login");
        if (optionLogin == null) {
            try {
                return readUserName();
            } catch (IOException ex) {
                throw new CommandException(ex);
            }
        } else if (!optionLogin.hasArgument()) {
            throw new CommandException("User name must be supplied!");
        }
        return optionLogin.getArgument();
    }
    
    private String getPassword(CommandLine commandLine) throws CommandException {
        CommandLineParser.Option optionPassword = commandLine.getOption("password");
        if (optionPassword == null) {
            try {
                return readPassword();
            } catch (IOException ex) {
                throw new CommandException(ex);
            }
        } else if (!optionPassword.hasArgument()) {
            throw new CommandException("Password must be supplied!");
        }
        return optionPassword.getArgument();
    }
    
    public String getUrl(CommandLine commandLine) throws CommandException {
        CommandLineParser.Option optionUrl = commandLine.getOption("url");
        if (optionUrl == null) {
            try {
                return readURL();
            } catch (IOException ex) {
                throw new CommandException(ex);
            }
        } else if (!optionUrl.hasArgument()) {
            throw new CommandException("JIRA URL must be supplied!");
        }
        return optionUrl.getArgument();
    }

    public void run(CommandLine commandLine, String issue) throws CommandException {
        String login = getUserName(commandLine);
        String password = getPassword(commandLine);
        String url = getUrl(commandLine);

        try {
            findTasks(url, login, password, issue);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    private void findTasks(String url, String login, String password, String issueName) throws URISyntaxException, JiraException {
        JiraAdapter jira = new JiraAdapter(url, login, password, true);
        List<SprintIssue> issues = jira.getOpenSprintIssues(issueName);
        if (issues == null) {
            System.out.println("No issues found");
        } else {
            for (SprintIssue issue : issues) {
                System.out.println(issue);
            }
        }
    }
    
}
