package cz.zoom.whiteboard.decoder;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.QRCode;
import cz.zoom.whiteboard.generator.Task;
import cz.zoom.whiteboard.generator.TaskParser;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.List;

public class TaskDecoder {
    private QRCode qrCode = new QRCode();
    private TaskParser taskParser = new TaskParser();
    
    public List<Task> decodeTasks(BufferedImage image) throws NotFoundException, FileNotFoundException {
        Result[] result = qrCode.decode(image);
        
        String yamlText = "";
        if (result == null) {
            return null;
        }
        for (Result res : result) {
            yamlText += res.getText() + "\n---\n";
        }
        System.out.println(result.length + " : " + yamlText);
        return taskParser.parseYamlText(yamlText);
    }
    
}
