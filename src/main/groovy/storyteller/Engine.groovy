package storyteller

class Engine {
    private final Script gameScript

    private Game game;
    private Binding binding;

    Engine(Script gameScript) {
        this.gameScript = gameScript;
    }

    def newGame() {
        game = new Game()
        binding = new Binding(game: game)
    }


}

