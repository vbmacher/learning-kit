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
        String[] args = new String[] { "--" + CommandLineParser.OPT_LOGIN, "test"};
        CommandLine result = parser.parse(args);
        
        Option user = result.getOption(CommandLineParser.OPT_LOGIN);
        assertEquals("test",user.getArguments()[0]);
    }
    
    @Test
    public void testLoginShortOption() {
        String[] args = new String[] { "-l", "test"};
        CommandLine result = parser.parse(args);
        
        Option user = result.getOption(CommandLineParser.OPT_LOGIN);
        assertEquals("test",user.getArguments()[0]);
    }
    
    @Test
    public void testOpenIssuesByIDLongOption() {
        String[] args = new String[] { "--" + CommandLineParser.OPT_OPENBYID, "38", "57"};
        CommandLine result = parser.parse(args);
        
        Option sprint = result.getOption(CommandLineParser.OPT_OPENBYID);
        assertEquals("38",sprint.getArguments()[0]);
        assertEquals("57",sprint.getArguments()[1]);
    }

    @Test
    public void testOpenIssuesByIDShortOption() {
        String[] args = new String[] { "-O", "38", "57"};
        CommandLine result = parser.parse(args);
        
        Option sprint = result.getOption(CommandLineParser.OPT_OPENBYID);
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

        Option user = result.getOption(CommandLineParser.OPT_LOGIN);
        assertEquals("user",user.getArguments()[0]);
        
        Option url = result.getOption(CommandLineParser.OPT_URL);
        assertEquals("http://jira:81/",url.getArguments()[0]);
    }
    
    
}
