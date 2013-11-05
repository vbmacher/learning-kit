package cz.zoom.whiteboard;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.CommandLineParser.CommandLine;
import cz.zoom.whiteboard.CommandLineParser.Option;
import cz.zoom.whiteboard.decoder.Whiteboard;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.SprintIssue;

public class Main {
    
    public static Result[] getQRCode(String fileName) throws NotFoundException, IOException {
        return QRCode.decode(ImageIO.read(new File(fileName)));
    }
    
    public static void usageAndExit() {
        CommandLineParser.usage();
        System.exit(1);
    }
    
    private static void renderYaml(String fileName) {
        try {
            Tasks generator = TasksFactory.createFromYamlFile(fileName);

            int i = 0;
            ImageIO.setUseCache(false);
            for (BufferedImage taskImage : generator.render()) {
                File file = new File("task-" + i + ".png");
                ImageIO.write(taskImage, "PNG", file);
            }
        } catch (Exception e) {
            System.err.println("Could not generate tasks: " + e.getMessage());
        }
    }
    
    private static void decodePNG(String pngFileName) {
        try {
            Result[] result = getQRCode(pngFileName);
            System.out.println("Recognized " + result.length + " codes.\n");
            for (Result res : result) {
                System.out.println(res.getText());
            }
        } catch (Exception e) {
            System.err.println("Could not dump QR Code: " + e.getMessage());
        }
    }
    
    private static void whiteboard() {
        Webcam.setHandleTermSignal(true);
        try {
            new Whiteboard().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Could not open whiteboard", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void findTasks(String url, String login, String password, String issueName) throws URISyntaxException, JiraException {
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
    
    public static void main( String[] args ) throws URISyntaxException, JiraException {
        CommandLine cmdLine = new CommandLineParser().parse(args);
        
        if (cmdLine.isEmpty()) {
            usageAndExit();
        }

        Iterator<Option> iterator = cmdLine.iterator();
        while (iterator.hasNext()) {
            Option option = iterator.next();
            
            if (option.getName().equals("render")) {
                if (option.hasArgument()) {
                    renderYaml(option.getArgument());
                } else {
                    usageAndExit();
                }
            }
            if (option.getName().equals("decode")) {
                if (option.hasArgument()) {
                    decodePNG(option.getArgument());
                } else {
                    usageAndExit();
                }
            }
            if (option.getName().equals("whiteboard")) {
                whiteboard();
                break;
            }
            if (option.getName().equals("story")) {
                if (!option.hasArgument()) {
                    usageAndExit();
                }
                String issue = option.getArgument();
                
                Option login = cmdLine.getOption("login");
                Option password = cmdLine.getOption("password");
                Option url = cmdLine.getOption("url");
                
                if ((login == null) || (password == null) || (url == null)) {
                    usageAndExit();
                }
                if (!login.hasArgument() || !password.hasArgument() || !url.hasArgument()) {
                    usageAndExit();
                }
                
                findTasks(url.getArgument(), login.getArgument(), password.getArgument(), issue);
            }
        }
    }
    
}
