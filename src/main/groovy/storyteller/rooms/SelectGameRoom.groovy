package storyteller.rooms

import java.awt.Point
import storyteller.gamemodel.Room
import storyteller.gui.Board
import storyteller.gui.Menu
import storyteller.gui.RoomComponent
import javafx.scene.control.Label
import storyteller.gui.Defaults
import storyteller.gui.Option
import storyteller.gamemodel.Engine

public class SelectGameRoom extends RoomComponent {
    private static final String IMAGE_FILE_NAME = "/storyteller.png"
    private final Menu menu

    public SelectGameRoom(Board board, RoomComponent previous) {
        super(new Room('selectGame',
                [
                    image:MainMenuRoom.class.getResourceAsStream(IMAGE_FILE_NAME)
                ]
            ), board, previous)

        def fileNames = loadGameFileNames()
        def menuOptions = fileNames.collectEntries() { gameFile ->
            [(gameFile.getName()): { loadGame(gameFile) } ]
        }

        def label = new Label('Select game:')
        label.setFont(Defaults.getDefaultFont())
        label.relocate(400, 380)

        def back = new Option('Back', { back() }).getNode()
        back.relocate(800, 680)

        menu = new Menu(menuOptions, 500,250)
        menu.relocate(400,420)
        getChildren().addAll(label, menu, back)
    }

    private def loadGameFileNames() {
        def files = []
        new File('.').eachFileRecurse() {
            if (it.getName().endsWith('.game') && !it.isDirectory()) {
                files << it
            }
        }
        return files
    }

    def loadGame(gameFile) {
        println "loading game ${gameFile}"
        def engine = new Engine(gameFile, board)
        try {
            engine.newGame()
        } catch (Exception e) {
            
        }
        engine.start()
    }

}

