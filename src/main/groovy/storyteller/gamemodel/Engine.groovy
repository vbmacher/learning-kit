package storyteller.gamemodel

import storyteller.gui.Board
// Thread-safe.
class Engine {
    private final String gameScript
    private Game game; // Guarded by "this"
    private Player player = new Player();
    private final Board board // Guarded by "this"

    Engine(File gameFile, Board canvas) {
        this.gameScript = gameFile.text
        this.board = canvas
    }

    Engine(String gameScript, Board board) {
        this.gameScript = gameScript
        this.board = board
    }

    def newGame() {
        stop()
        synchronized(this) {
            game = new Game()
            new GroovyShell(new Binding(game: game, player:player)).evaluate(gameScript)
            board?.setCurrentRoom(game.rooms.current)
        }
    }

    def synchronized getGameName() {
        return game.name
    }

    def synchronized start() {
    }

    def synchronized stop() {

    }
}

