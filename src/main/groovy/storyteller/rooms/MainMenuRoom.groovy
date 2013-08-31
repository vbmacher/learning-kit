package storyteller.rooms

import java.awt.Graphics
import java.awt.Point
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import storyteller.gui.RoomComponent
import storyteller.gui.Menu
import storyteller.gui.Board
import storyteller.gamemodel.Room
import storyteller.Main

public class MainMenuRoom extends RoomComponent {
    private static final String IMAGE_FILE_NAME = "/storyteller.png"
    private final Menu menu
    private final Board board

    MainMenuRoom(Board board) {
        super(new Room('mainMenu',
            [
                image:MainMenuRoom.class.getResourceAsStream(IMAGE_FILE_NAME)
            ]
        ))
        this.board = board
        menu = new Menu(
            [
                'Start new game': {startNewGame()},
                'Exit': {gameExit()}
            ], new Point(550,500))
        getChildren().add(menu)
    }

    def startNewGame() {
        board.setCurrentRoom(new SelectGameRoom(board))
    }

    def gameExit() {
        board.setCurrentRoom(null)
        Main.requestShutdown()
    }


}

