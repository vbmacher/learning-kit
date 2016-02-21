package storyteller.gamemodel

import storyteller.gui.Board
import storyteller.rooms.GameRoom
import javax.swing.JOptionPane

// Not thread-safe.
class Engine {
    private final String gameScript
    private Game game;
    private Player player = new Player();
    private final Board board

    Engine(File gameFile, Board canvas) {
        this.gameScript = gameFile.text
        this.board = canvas
    }

    Engine(String gameScript, Board board) {
        this.gameScript = gameScript
        this.board = board
    }

    def newGame() {
        game = new Game()
        new GroovyShell(new Binding(game: game, player:player)).evaluate(gameScript)
    }

    def getGameName() {
        return game.name
    }

    def start() {
        def gameRoom = new GameRoom(game.rooms.current, board)
        board.setCurrentRoom(gameRoom)
    }

    def stop() {

    }
}

