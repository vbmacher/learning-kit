package cz.zoom.whiteboard.cmdline;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import cz.zoom.whiteboard.QRCode;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DecodePNG implements CommandLineMediator.Command {

    private Result[] getQRCode(String fileName) throws NotFoundException, IOException {
        return QRCode.decode(ImageIO.read(new File(fileName)));
    }
    
    
    public void run(CommandLine commandLine, String pngFileName) throws CommandException {
        try {
            Result[] result = getQRCode(pngFileName);
            System.out.println("Recognized " + result.length + " codes.\n");
            for (Result res : result) {
                System.out.println(res.getText());
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
}
