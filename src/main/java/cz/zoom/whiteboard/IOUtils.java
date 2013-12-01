package cz.zoom.whiteboard;

import java.io.Console;
import java.io.IOException;

public class IOUtils {

    public static String readString(String message) throws IOException {
        System.out.print(message);
        Console c = System.console();
        if (c != null) {
            return c.readLine();
        }
        return null;
    }

    public static String readStringSilently(String message) throws IOException {
        System.out.print(message);
        Console c = System.console();
        if (c != null) {
            return new String(c.readPassword());
        }
        return null;
    }

}
