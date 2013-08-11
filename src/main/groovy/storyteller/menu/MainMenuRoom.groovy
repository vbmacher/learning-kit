package storyteller.menu

import java.awt.Graphics
import java.awt.Point
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import storyteller.Room
import storyteller.Board
import storyteller.Main

class MainMenuRoom extends Room {
    private static final String IMAGE_FILE_NAME = "/storyteller.png"
    private final Menu menu
    private final Board board

    MainMenuRoom(Board board) {
        super('mainMenu', [:], [:])
        updateImage(MainMenuRoom.class.getResource(IMAGE_FILE_NAME))

        this.board = board
        menu = new Menu(
            [
                'Start new game': {startNewGame()},
                'Exit': {gameExit()}
            ], new Point(550,500))
        objects.put(menu.objectName, menu)
    }

    def startNewGame() {
      board.setCurrentRoom(new SelectGameRoom(board))
    }

    def gameExit() {
        Main.requestShutdown()
    }


}

