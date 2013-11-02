package cz.zoom.whiteboard;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.CommandLineParser.CommandLine;
import cz.zoom.whiteboard.decoder.Whiteboard;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Main {
    
    public static Result[] getQRCode(String fileName) throws NotFoundException, IOException {
        return QRCode.decode(ImageIO.read(new File(fileName)));
    }
    
    public static void usageAndExit() {
        System.err.println("Usage: whiteboard.jar -e <YAML file> | -d <PNG file> | -w");
        System.exit(1);
    }
    
    
    public static void main( String[] args ) {
        CommandLine cmdLine = new CommandLineParser().parse(args);
        
        if (cmdLine.has("encode") && cmdLine.hasArgument("encode")) {
            try {
                TaskTransformations generator = new TaskTransformations();
                
                int i = 0;
                ImageIO.setUseCache(false);
                for (BufferedImage taskImage : generator.renderAll(cmdLine.getArgument("encode"))) {
                    File file = new File("task-" + i + ".png");
                    ImageIO.write(taskImage, "PNG", file);
                }
            } catch (Exception e) {
                System.err.println("Could not generate tasks: " + e.getMessage());
            }
        } else if (cmdLine.has("decode") && cmdLine.hasArgument("decode")) {
            try {
                Result[] result = getQRCode(cmdLine.getArgument("decode"));
                System.out.println("Recognized " + result.length + " codes.\n");
                for (Result res : result) {
                    System.out.println(res.getText());
                }
            } catch (Exception e) {
                System.err.println("Could not dump QR Code: " + e.getMessage());
            }
        } else if (cmdLine.has("whiteboard")) {
            Webcam.setHandleTermSignal(true);
            try {
                new Whiteboard().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Could not open whiteboard", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            usageAndExit();
        }
    }
    
}
