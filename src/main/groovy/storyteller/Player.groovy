package storyteller

import java.util.concurrent.atomic.AtomicInteger

// Thread-safe.
class Player extends GameObject {
    private final static AtomicInteger ID = new AtomicInteger(0);
    private final playerName

    Player() {
        super('player')
        this.playerName = 'player_' + ID.incrementAndGet()
    }

    Player(playerName) {
        super('player')
        this.playerName = playerName
    }

    def String toString() {
        "{Player=$playerName}"
    }

}

