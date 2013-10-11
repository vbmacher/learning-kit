package cz.zoom.whiteboard.generator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class TaskParser {
    
    private List<Task> yamlIntoTasks(Iterable<Object> yamlData) {
        List<Task> tasks = new ArrayList<Task>();
        
        for (Object task : yamlData) {
            try {
                tasks.add(new Task((Map<String, Object>) task));
            } catch (Exception e) {
                System.err.println("Could not generate task: " + e.getMessage());
            }
        }
        return Collections.unmodifiableList(tasks);
    }
    
    public List<Task> parseYaml(String yamlFile) throws FileNotFoundException {
        return yamlIntoTasks(new Yaml().loadAll(new FileReader(yamlFile)));
    }
    
    public List<Task> parseYamlText(String yamlText) throws FileNotFoundException {
        return yamlIntoTasks(new Yaml().loadAll(yamlText));
    }
    
    public Object extract(String key, Object yamlData) {
        return ((Map<String, Object>)yamlData).get(key);
    }
    
    public String dump(Object yamlData) {
        Yaml yaml = new Yaml();
        return yaml.dump(yamlData);
    }
    
}
