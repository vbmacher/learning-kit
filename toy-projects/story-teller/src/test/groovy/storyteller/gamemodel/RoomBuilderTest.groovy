package storyteller.gamemodel

class RoomBuilderTest extends GroovyTestCase {

    def Map rooms
    def Map objects

    void setUp() {
        rooms = new HashMap()
        objects = new HashMap()
    }

    void testCreateRoom() {
        def builder = new RoomBuilder(rooms, objects)

        def room = builder.start(
            name:'Sharewood land',
            image:'forest.png',
            info: 'What you gonna do?',
            actions: [
                castle: 'Walk on the road',
                forest: 'Look into the forest'
            ],
            objects: ['sword', 'player']
        )
        assert room.name == 'Sharewood land'
        assert room.image == null
        assert room.actions == [
            castle: 'Walk on the road',
            forest: 'Look into the forest'
        ]
        assert room.objects == [:]
        assert rooms.start == room
    }

    void testObjectRecognition() {
        def gameObject = new GameObject('sword')
        objects.put('sword', gameObject)
        def builder = new RoomBuilder(rooms, objects)

        def room = builder.someRoom(
            objects: [ 'sword' ]
        )

        assert room.objects == [ 'sword':gameObject ]
        assert room.objects.sword == gameObject
    }

    void testInsertAndRemoveObject() {
        def gameObject = new GameObject('sword')
        objects.put('sword', gameObject)
        def builder = new RoomBuilder(rooms, objects)

        def room = builder.someRoom(name:'room')
        room.put(gameObject)

        assert room.objects == [ 'sword':gameObject ]
        assert room.objects.sword == gameObject

        def tmp = room.take('sword')
        assert tmp == gameObject
        assert room.objects == [:]
        assert room.objects.sword == null
    }
}

