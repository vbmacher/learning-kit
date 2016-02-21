package storyteller.gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainWindow {
    private final Scene scene;
    private final Stage stage;

    public MainWindow(Stage stage, Board board) {
        this.stage = stage;

        Group root = new Group();
        scene = new Scene(root, board.getWidth(), board.getHeight(), Color.rgb(0,0,0));
        scene.getStylesheets().add("css/listview.css");

        root.getChildren().add(board);
        stage.setTitle("Story teller");
        stage.setResizable(false);
        stage.setScene(scene);
        board.setFocusTraversable(true);
        board.onKeyPressedProperty().setValue(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ESCAPE) {
                    dispose();
                }
            }
        });
    }

    public void show() {
        stage.show();
    }

    public void dispose() {
        stage.close();
    }

}
