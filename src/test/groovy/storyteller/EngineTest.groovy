package storyteller

class EngineTest extends GroovyTestCase {

    void testGameAndPlayerInsideGameScript() {
        Engine engine = new Engine("""
            assert game instanceof storyteller.Game
            assert player instanceof storyteller.Player
        """, null)
        engine.newGame()
    }

    void testSyntaxErrorInGameScript() {
        Engine engine = new Engine(" dffg spsodfk sp pw4443 55 srfscs", null)

        shouldFail {
            engine.newGame()
        }
    }

    void testGameClosure() {
        Engine engine = new Engine("""
            game {
                name = 'My game'
            }
            """, null)
        engine.newGame()
        assert engine.getGameName() == 'My game'
    }
}

