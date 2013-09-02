package storyteller;

import storyteller.gui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import storyteller.gui.Board;
import storyteller.rooms.MainMenuRoom;

public class Main extends Application {
    private static MainWindow window;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        Board board = new Board();
        board.setFirstRoom(new MainMenuRoom(board));
        window = new MainWindow(primaryStage, board);
        window.show();
    }

    public static void requestShutdown() {
        window.dispose();
    }

}
