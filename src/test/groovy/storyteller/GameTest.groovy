package storyteller

class GameTest extends GroovyTestCase {
    def Game game

    void setUp() {
        game = new Game()
    }

    void testMakeObjects() {
        game.objects {
            sword(
                name:'Kings sword',
                position: [x:500,y:1000],
                image: 'sword.png',
            )

            dragon(
                name:'Dragon Helicon',
                position:[10,10],
                image:'dragon.png'
            )
        }
        assert game.objects['sword'] instanceof GameObject
        assert game.objects['dragon'] instanceof GameObject

        assert game.objects.sword.name == 'Kings sword'
    }

    void testMakeRooms() {
        game.rooms {
            start(
                name:'Sharewood land',
                image:'forest.png',
                info: 'What you gonna do?',
                actions: [
                    castle: 'Walk on the road',
                    forest: 'Look into the forest'
                ],
                objects: ['sword']
            )
        }

        assert game.rooms['start'] instanceof Room
        assert game.rooms['start'].name == 'Sharewood land'
    }

    void testAction() {
        game.objects { sword(name:'Kings sword'); dragon(name:'Dragon') }
        game.rooms { start(name:'Sharewood land', objects: [ 'sword', 'dragon' ]); forest(name:'Deep forest') }
        game.action { rooms, objects ->
            objects {
                dragon.lives = 100
                sword.moveable = true
                sword.useable = { currentRoom ->
                    if (currentRoom.dragon?.kill(20)) {
		        WINNER
                    }
                }
                dragon.timer = [ start: 1, repeat: 1,
                    action: {
                        if (player.kill(20)) {
                            LOSER
                        }
		    }
                ]
            }
            rooms {
                current = start
                forest.init = { LOSER }
            }
        }

        assert game.objects.size() == 2
        assert game.rooms.size() == 3 // including current
        assert game.objects.dragon.lives == 100
        assert game.objects.sword.moveable == true
        assert game.objects.sword.useable instanceof Closure
        assert game.objects.dragon.timer instanceof Map
        assert game.objects.dragon.timer.action instanceof Closure
        assert game.rooms.current == game.rooms.start
        assert game.rooms.forest.init instanceof Closure
    }
}

