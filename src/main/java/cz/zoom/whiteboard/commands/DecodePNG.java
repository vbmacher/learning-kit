package cz.zoom.whiteboard.commands;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.QRCode;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.cmdline.CommandException;
import cz.zoom.whiteboard.cmdline.CommandLine;
import cz.zoom.whiteboard.cmdline.CommandLineParser;
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
                out.println(res.getText());
                if (yamlOutput) {
                    out.println("---");
                }
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
        
}
