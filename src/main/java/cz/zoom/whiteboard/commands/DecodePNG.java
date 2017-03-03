package cz.zoom.whiteboard.commands;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.QRCode;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
import cz.zoom.whiteboard.task.TaskFactory;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DecodePNG extends Command {

    private Result[] getQRCode(String fileName) throws NotFoundException, IOException {
        return QRCode.decode(ImageIO.read(new File(fileName)));
    }
    
    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        if (arguments.length < 1) {
            throw new CommandException("DecodePNG: argument is needed!");
        }

        boolean yamlOutput = commandLine.hasOption(CommandLineParser.OPT_YAML);
        try {
            Result[] result = getQRCode(arguments[0]);
            if (!yamlOutput) {
                out.println("Recognized " + result.length + " codes.\n");
            }
            for (Result res : result) {
                String text = res.getText();
                if (yamlOutput) {
                    try {
                        TaskFactory.createFromYamlText(text);
                        out.println(text);
                        out.println("---");
                    } catch (Exception e) {
                    }
                } else {
                    out.println(text);
                }
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
        
}
