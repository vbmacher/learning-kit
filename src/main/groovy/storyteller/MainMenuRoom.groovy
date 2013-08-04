package storyteller

import java.awt.Graphics
import java.awt.Point
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

class MainMenuRoom extends Room {
    private static final String IMAGE_FILE_NAME = "/storyteller.png"
    private final Menu menu

    MainMenuRoom() {
        super('mainMenu', [:], [:])
        this.dialog = dialog
        updateImage(MainMenuRoom.class.getResource(IMAGE_FILE_NAME))

        menu = new Menu(
            [
                'Start new game': {startNewGame()},
                'Exit': {gameExit()}
            ], new Point(550,500))
        objects.put(menu.objectName, menu)
    }

    def startNewGame() {
        println 'Starting new game...'
    }

    def gameExit() {
        Main.requestShutdown()
    }


}

