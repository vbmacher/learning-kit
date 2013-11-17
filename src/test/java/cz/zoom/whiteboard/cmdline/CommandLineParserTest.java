package cz.zoom.whiteboard.cmdline;

import cz.zoom.whiteboard.cmdline.CommandLineParser.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineParserTest {
    
    private CommandLineParser parser;
    
    @Before
    public void setUp() {
        parser = new CommandLineParser();
    }
    
    @Test
    public void testLoginLongOption() {
        String[] args = new String[] { "--login", "test"};
        CommandLine result = parser.parse(args);
        
        Option user = result.getOption("login");
        assertEquals("test",user.getArguments()[0]);
    }
    
    @Test
    public void testLoginShortOption() {
        String[] args = new String[] { "-l", "test"};
        CommandLine result = parser.parse(args);
        
        Option user = result.getOption("login");
        assertEquals("test",user.getArguments()[0]);
    }
    
    @Test
    public void testSprintByIDLongOption() {
        String[] args = new String[] { "--sprint", "38", "57"};
        CommandLine result = parser.parse(args);
        
        Option sprint = result.getOption("sprint");
        assertEquals("38",sprint.getArguments()[0]);
        assertEquals("57",sprint.getArguments()[1]);
    }

    @Test
    public void testSprintByIDShortOption() {
        String[] args = new String[] { "-s", "38", "57"};
        CommandLine result = parser.parse(args);
        
        Option sprint = result.getOption("sprint");
        assertEquals("38",sprint.getArguments()[0]);
        assertEquals("57",sprint.getArguments()[1]);
    }

    @Test
    public void testTwoParameters() {
        String[] args = new String[] {
            "-l", "user",
            "-u", "http://jira:81/",
        };
        CommandLine result = parser.parse(args);

        Option user = result.getOption("login");
        assertEquals("user",user.getArguments()[0]);
        
        Option url = result.getOption("url");
        assertEquals("http://jira:81/",url.getArguments()[0]);
    }
    
    
}
