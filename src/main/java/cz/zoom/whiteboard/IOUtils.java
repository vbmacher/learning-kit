package cz.zoom.whiteboard;

import java.io.IOException;
import java.io.PrintStream;

public class IOUtils {
    
    public static String readString() throws IOException {
        char c;
        String str = "";
        
        do {
            c = (char)System.in.read();
            str += c;
        } while (c!= '\n');
        return str.substring(0, str.length()-1);
    }
    
    public static String readStringSilently() throws IOException {
        PrintStream out = System.out;
        PrintStream err = System.err;
        
        System.setErr(null);
        System.setOut(null);
        try {
            return readString();
        } finally {
            System.setErr(err);
            System.setOut(out);
        }
    }
    
}
