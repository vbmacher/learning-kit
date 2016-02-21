package storyteller.gamemodel

class EngineTest extends GroovyTestCase {

    void testGameAndPlayerInsideGameScript() {
        Engine engine = new Engine("""
            assert game instanceof storyteller.gamemodel.Game
            assert player instanceof storyteller.gamemodel.Player
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

