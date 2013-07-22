package storyteller

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

// Thread safe.
class Game {
    static final int WINNER = 0;
    static final int LOSER = 1;
    static final int CONTINUE = 2;

    final ConcurrentMap<String, GameObject> objects = new ConcurrentHashMap<String,GameObject>();
    final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();
    def volatile String name = 'A story'

    Game() {
        objects.put('player', new Player())
    }

    def objects(Closure closure) {
        def builder = new GameObjectBuilder(objects)
        closure.delegate = builder
        closure()
    }

    def rooms(Closure closure) {
        def builder = new RoomBuilder(rooms, objects)
        closure.delegate = builder
        closure()
    }

    def action(Closure closure) {
        closure.delegate = Game
        closure(rooms, objects)
    }
}

