package cz.zoom.whiteboard.task;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.QRCode;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.SprintIssue;
import org.yaml.snakeyaml.Yaml;

public class TasksFactory {
    
    private static Tasks createFromYamlData(Iterable<Object> yamlData) throws TaskException {
        List<Task> tasks = new ArrayList<Task>();

        for (Object task : yamlData) {
            if (task != null) {
                tasks.add(new Task((Map<String, String>) task));
            }
        }
        return new Tasks(tasks.toArray(new Task[0]));
    }
    
    public static Tasks createFromYamlFile(String yamlFile) throws FileNotFoundException, TaskException {
        return createFromYamlData(new Yaml().loadAll(new FileReader(yamlFile)));
    }
    
    public static Tasks createFromYamlText(String yamlText) throws TaskException {
        return createFromYamlData(new Yaml().loadAll(yamlText));
    }
    
    public static Tasks createFromSprintIssues(List<SprintIssue> issues) throws JiraException, TaskException {
        List<Task> tasks = new ArrayList<Task>();
        
        for (SprintIssue issue : issues) {
            tasks.add(new Task(issue.getJiraIssue()));
        }
        return new Tasks(tasks.toArray(new Task[0]));
    }
    
    public static Tasks createFromJiraIssues(List<Issue> issues) throws JiraException, TaskException {
        List<Task> tasks = new ArrayList<Task>();
        
        for (Issue issue : issues) {
            tasks.add(new Task(issue));
        }
        return new Tasks(tasks.toArray(new Task[0]));
    }

    public static Tasks createFromImage(BufferedImage image) throws NotFoundException, FileNotFoundException, TaskException {
        Result[] result = QRCode.decode(image);
        
        String yamlText = "";
        if (result == null) {
            return null;
        }
        for (Result res : result) {
            yamlText += res.getText() + "\n---\n";
        }
        return createFromYamlText(yamlText);
    }
    
}
