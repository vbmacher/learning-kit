package storyteller;

import java.io.File;
import javax.swing.SwingUtilities;

public class Main {

    public static void startGame(String fileName, RoomCanvas canvas) {
        Engine engine = new Engine(new File(fileName), canvas);


    }

    public static void main(String[] args) {
        final RoomCanvas canvas = new RoomCanvas(new MainMenuRoom());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainDialog(canvas).setVisible(true);
            }
        });
    }

}
