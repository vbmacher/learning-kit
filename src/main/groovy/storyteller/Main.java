package storyteller;

import java.io.File;
import javax.swing.SwingUtilities;

public class Main {
    private static MainDialog dialog;

    public static void startGame(String fileName, Board canvas) {
        Engine engine = new Engine(new File(fileName), canvas);


    }

    public static void requestShutdown() {
        dialog.dispose();

    }

    public static void main(String[] args) {
        final Board board = Board.newInstance(new MainMenuRoom());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
               dialog = MainDialog.newInstance(board);
               dialog.setVisible(true);
            }
        });
    }

}
