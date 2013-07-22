package storyteller

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class Game {
    static final int WINNER = 0;
    static final int LOSER = 1;
    static final int CONTINUE = 2;

    static final ConcurrentMap<String, GameObject> objects = new ConcurrentHashMap<String,GameObject>();
    static final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();

    static {
        objects.put('player', new Player())
    }

    def static volatile String name = 'A story'

    def static objects(Closure closure) {
        def builder = new GameObjectBuilder(objects)
        closure.delegate = builder
        closure()
    }

    def static rooms(Closure closure) {
        def builder = new RoomBuilder(rooms, objects)
        closure.delegate = builder
        closure()
    }

    def static action(Closure closure) {
        closure.delegate = Game
        closure(rooms, objects)
    }
}

