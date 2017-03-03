package cz.zoom.whiteboard.task;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.Code;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class TaskFactory {
    
    private static Task createFromYamlData(Object yamlData) throws TaskException {
        return new Task((Map<String, String>) yamlData);
    }
    
    public static Task createFromYamlFile(String yamlFile) throws FileNotFoundException, TaskException {
        return createFromYamlData(new Yaml().load(new FileReader(yamlFile)));
    }
    
    public static Task createFromYamlText(String yamlText) throws TaskException {
        return createFromYamlData(new Yaml().load(yamlText));
    }
    
    public static Task createFromImage(BufferedImage image) throws NotFoundException, FileNotFoundException, TaskException {
        Result[] result = Code.decode(image);
        
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
