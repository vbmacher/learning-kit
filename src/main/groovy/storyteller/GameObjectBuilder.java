package storyteller;

import groovy.util.BuilderSupport;
import java.util.Iterator;
import java.util.Map;

public class GameObjectBuilder extends BuilderSupport {
    private final static String MISSING_ERROR = "Missing object attributes";
    private final static String CANNOT_HAVE_CHILDREN_ERROR = "Objects cannot have children objects";

    private final Map<String, GameObject> gameObjects;

    public GameObjectBuilder(Map<String, GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    @Override
    protected void setParent(Object parent, Object child) {
        throw new UnsupportedOperationException(CANNOT_HAVE_CHILDREN_ERROR);
    }

    public Iterator iterator() {
        return gameObjects.values().iterator();
    }

    @Override
    protected Object createNode(Object name) {
        throw new UnsupportedOperationException(MISSING_ERROR);
    }

    @Override
    protected Object createNode(Object name, Object value) {
        throw new UnsupportedOperationException(MISSING_ERROR);
    }

    @Override
    protected Object createNode(Object name, Map attributes) {
        GameObject gameObject = new GameObject(name, attributes);
        gameObjects.put((String)name, gameObject);
        return gameObject;
    }

    @Override
    protected Object createNode(Object name, Map attributes, Object value) {
        throw new UnsupportedOperationException(MISSING_ERROR);
    }

}
