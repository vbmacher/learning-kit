package storyteller

// Thread-safe.
class Engine {
    private final String gameScript
    private Game game; // Guarded by "this"
    private Player player = new Player();

    Engine(File gameFile) {
        this.gameScript = gameFile.text
    }

    Engine(String gameScript) {
        this.gameScript = gameScript
    }

    def newGame() {
        stop()
        synchronized(this) {
            game = new Game()
            new GroovyShell(new Binding(game: game, player:player)).evaluate(gameScript)
        }
    }

    def synchronized start() {
    }

    def synchronized stop() {

    }
}

