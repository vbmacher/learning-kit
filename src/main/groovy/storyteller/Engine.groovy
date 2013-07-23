package storyteller

// Thread-safe.
class Engine {
    private final String gameScript
    private Game game; // Guarded by "this"
    private Player player = new Player();
    private final RoomCanvas roomCanvas // Guarded by "this"

    Engine(File gameFile, RoomCanvas canvas) {
        this.gameScript = gameFile.text
        this.roomCanvas = canvas
    }

    Engine(String gameScript, RoomCanvas canvas) {
        this.gameScript = gameScript
        this.roomCanvas = canvas
    }

    def newGame() {
        stop()
        synchronized(this) {
            game = new Game()
            new GroovyShell(new Binding(game: game, player:player)).evaluate(gameScript)
            roomCanvas?.setCurrentRoom(game.rooms.current)
        }
    }

    def synchronized start() {
    }

    def synchronized stop() {

    }
}

