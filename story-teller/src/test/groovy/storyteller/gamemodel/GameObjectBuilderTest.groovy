package storyteller.gamemodel;

import java.awt.Point
class GameObjectBuilderTest extends GroovyTestCase {
    private Map<String, GameObject> objects;

    void setUp() {
        objects = new HashMap<String, GameObject>();
    }

    void tearDown() {
        objects.clear()
    }

    void testGameObject() {
        def builder = new GameObjectBuilder(objects);
        def sword = builder.sword(
            name:'Kings sword',
            position: [x:500,y:1000],
            image: 'sword.png',
            moveable: true
        )
        assert sword instanceof GameObject
        assert sword.name == 'Kings sword'
        assert sword.objectName == 'sword'
        assert sword.position() == new Point(500,1000)
        assert sword.image == null // image file does not exist so it's not converted into object
        assert sword.moveable == true
    }

    void testGameObjectPositionAsList() {
        def builder = new GameObjectBuilder(objects)
        def myObj = builder.obj position:[10,10]
        assert myObj.position() == new Point(10,10)
    }

    void testGameObjectWithoutParameters() {
        def builder = new GameObjectBuilder(objects);
        shouldFail(UnsupportedOperationException) {
            def someObject = builder.someObject()
        }
    }

    void testGameObjectWithValue() {
        def builder = new GameObjectBuilder(objects);
        shouldFail(UnsupportedOperationException) {
            def obj = builder.obj("value")
        }
    }

    void testInsertActionIntoGameObject() {
        def builder = new GameObjectBuilder(objects);
        def sword = builder.sword(name:'Kings sword', room:'start', position: [500,1000], image: 'sword.png', moveable: true)
        sword.use = { room, player ->
            player.win()
        }
    }

    void testGameObjectHierarchy() {
        shouldFail {
            def obj = new GameObjectBuilder(objects).someObject(name:'King', position: [0,0]) {
                anotherObject position: [5,5], name:'Kings crown'
            }
        }
    }

}
