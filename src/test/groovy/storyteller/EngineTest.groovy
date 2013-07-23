package storyteller

class EngineTest extends GroovyTestCase {

    void testGameAndPlayerInsideGameScript() {
        Engine engine = new Engine("""
            package storyteller

            assert game instanceof Game
            assert player instanceof Player
        """, null)
        engine.newGame()
    }

    void testSyntaxErrorInGameScript() {
        Engine engine = new Engine(" dffg spsodfk sp pw4443 55 srfscs", null)

        shouldFail {
            engine.newGame()
        }
    }
}

