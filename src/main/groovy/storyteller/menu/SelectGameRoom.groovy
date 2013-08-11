package storyteller.menu

import storyteller.Room
import storyteller.Board
import java.awt.Point

public class SelectGameRoom extends Room {
    private static final String IMAGE_FILE_NAME = "/storyteller.png"
    private final Board board
    private final Menu menu

    public SelectGameRoom(Board board) {
        super('selectGame', [:], [:])
        updateImage(SelectGameRoom.class.getResource(IMAGE_FILE_NAME))
        this.board = board

        def fileNames = loadGameFileNames()
        def menuOptions = fileNames.collectEntries() {
            [(it): { loadGame(it) } ]
        }

        menu = new Menu(menuOptions, new Point(400,400))
        objects.put(menu.objectName, menu)

        def label = new Label(new Point(400, 380), 'Select game:')
        objects.put(label.objectName, label)
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

