package storyteller

class GameTest extends GroovyTestCase {

    void tearDown() {
        Game.objects.clear()
        Game.rooms.clear()
    }

    void testMakeObjects() {
        Game.objects {
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
        assert Game.objects['sword'] instanceof GameObject
        assert Game.objects['dragon'] instanceof GameObject

        assert Game.objects.sword.name == 'Kings sword'
    }

    void testMakeRooms() {
        Game.rooms {
            start(
                name:'Sharewood land',
                image:'forest.png',
                info: 'What you gonna do?',
                actions: [
                    castle: 'Walk on the road',
                    forest: 'Look into the forest'
                ],
                objects: ['sword', 'player']
            )
        }

        assert Game.rooms['start'] instanceof Room
        assert Game.rooms['start'].name == 'Sharewood land'
    }

    void testAction() {
        Game.objects { sword(name:'Kings sword'); dragon(name:'Dragon') }
        Game.rooms { start(name:'Sharewood land', objects: [ 'sword', 'dragon' ]); forest(name:'Deep forest') }
        Game.action { rooms, objects ->
            objects.with {
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
            rooms.with {
                current = start
                forest.init = { LOSER }
            }
        }

        assert Game.objects.size() == 2
        assert Game.rooms.size() == 3
        assert Game.objects.dragon.lives == 100
        assert Game.objects.sword.moveable == true
        assert Game.objects.sword.useable instanceof Closure
        assert Game.objects.dragon.timer instanceof Map
        assert Game.objects.dragon.timer.action instanceof Closure
        assert Game.rooms.current == Game.rooms.start
        assert Game.rooms.forest.init instanceof Closure
    }
}

