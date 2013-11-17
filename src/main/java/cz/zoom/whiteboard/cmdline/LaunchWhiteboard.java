package cz.zoom.whiteboard.cmdline;

import com.github.sarxos.webcam.Webcam;
import cz.zoom.whiteboard.cmdline.Command;
import cz.zoom.whiteboard.decoder.Whiteboard;

public class LaunchWhiteboard extends Command {

    public void run(CommandLine commandLine, String[] arguments) throws CommandException {
        Webcam.setHandleTermSignal(true);
        try {
            new Whiteboard().setVisible(true);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

}
