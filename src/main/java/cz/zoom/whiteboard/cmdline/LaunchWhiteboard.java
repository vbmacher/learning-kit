package cz.zoom.whiteboard.cmdline;

import com.github.sarxos.webcam.Webcam;
import cz.zoom.whiteboard.decoder.Whiteboard;

public class LaunchWhiteboard implements CommandLineMediator.Command {

    public void run(CommandLine commandLine, String argument) throws CommandException {
        Webcam.setHandleTermSignal(true);
        try {
            new Whiteboard().setVisible(true);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
    
}
