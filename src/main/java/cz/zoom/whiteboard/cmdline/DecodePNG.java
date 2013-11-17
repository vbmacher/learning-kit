package cz.zoom.whiteboard.cmdline;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.QRCode;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DecodePNG extends Command {

    private Result[] getQRCode(String fileName) throws NotFoundException, IOException {
        return QRCode.decode(ImageIO.read(new File(fileName)));
    }
    
    public void run(CommandLine commandLine, String[] pngFileName) throws CommandException {
        boolean yamlOutput = commandLine.hasOption("yaml");
        try {
            Result[] result = getQRCode(pngFileName[0]);
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
