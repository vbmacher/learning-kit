package storyteller.rooms

import java.awt.Point
import storyteller.gamemodel.Room
import storyteller.gui.Board
import storyteller.gui.Menu
import storyteller.gui.RoomComponent
import javafx.scene.control.Label
import storyteller.gui.DialogComponent

public class SelectGameRoom extends RoomComponent {
    private static final String IMAGE_FILE_NAME = "/storyteller.png"
    private Board board
    private final Menu menu

    public SelectGameRoom(Board board) {
       super(new Room('selectGame',
            [
                image:MainMenuRoom.class.getResourceAsStream(IMAGE_FILE_NAME)
            ]
        ))
        this.board = board

        def fileNames = loadGameFileNames()
        def menuOptions = fileNames.collectEntries() {
            [(it): { loadGame(it) } ]
        }

        def label = new Label('Select game:')
        label.setFont(DialogComponent.getDefaultFont())
        label.relocate(400, 380)

        menu = new Menu(menuOptions, new Point(400,420))
        getChildren().addAll(label, menu)
    }

    private def loadGameFileNames() {
        def files = []
        new File('.').eachFileRecurse() {
            if (it.getName().endsWith('.game') && !it.isDirectory()) {
                files << it.getName()
            }
        }
        return files
    }

    def loadGame(name) {

    }

}

