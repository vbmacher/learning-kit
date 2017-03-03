package cz.zoom.whiteboard;

import java.io.IOException;
import java.io.PrintStream;

public class ConnectionDetails {
    private String userName;
    private String password;
    private String url;

    public ConnectionDetails(String userName, String password, String url) {
        this.userName = userName;
        this.password = password;
        this.url = url;
    }

    public String getURL() throws IOException {
        if (url == null) {
            url = IOUtils.readString("Enter JIRA URL: ");
        }
        return url;
    }

    public String getUserName() throws IOException {
        if (userName == null) {
            userName = IOUtils.readString("Enter user name: ");
        }
        return userName;
    }

    public String getPassword() throws IOException {
        if (password == null) {
            password = IOUtils.readStringSilently("Enter password: ");
        }
        return password;
    }

}
