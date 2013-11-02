package cz.zoom.whiteboard;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class TaskTransformations {

    public static BufferedImage[] renderAll(String yamlFile) throws FileNotFoundException {
        List<BufferedImage> taskImages = new ArrayList<BufferedImage>();
        for (Task task : parseYaml(yamlFile)) {
            try {
                taskImages.add(task.render());
            } catch (Exception e) {
                System.err.println("Could not render task QR code: " + e.getMessage());
            }
        }
        return taskImages.toArray(new BufferedImage[0]);
    }
    
    public static Task[] decodeAll(BufferedImage image) throws NotFoundException, FileNotFoundException {
        Result[] result = QRCode.decode(image);
        
        String yamlText = "";
        if (result == null) {
            return null;
        }
        for (Result res : result) {
            yamlText += res.getText() + "\n---\n";
        }
        System.out.println(result.length + " : " + yamlText);
        return TaskTransformations.parseYamlText(yamlText);
    }

    private static Task[] convertFromYamlData(Iterable<Object> yamlData) {
        List<Task> tasks = new ArrayList<Task>();
        
        for (Object task : yamlData) {
            try {
                tasks.add(new Task((Map<String, String>) task));
            } catch (Exception e) {
                System.err.println("Could not generate task: " + e.getMessage());
            }
        }
        return tasks.toArray(new Task[0]);
    }
    
    public static Task[] parseYaml(String yamlFile) throws FileNotFoundException {
        return convertFromYamlData(new Yaml().loadAll(new FileReader(yamlFile)));
    }
    
    public static Task[] parseYamlText(String yamlText) {
        return convertFromYamlData(new Yaml().loadAll(yamlText));
    }
    
    public static String dumpYaml(Object yamlData) {
        Yaml yaml = new Yaml();
        return yaml.dump(yamlData);
    }
}
