package storyteller.gamemodel

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.awt.Graphics
import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.imageio.ImageIO;
import javafx.scene.canvas.GraphicsContext
import javafx.scene.Group
import storyteller.gui.VisibleObject

// Thread-safe only when using methods inside this class.
class Room extends VisibleObject {
    protected final ConcurrentMap<String,GameObject> objects = new ConcurrentHashMap<String, GameObject>()

    Room(objectName, Map attributes) {
        this(objectName, attributes, [:])
    }

    Room(objectName, Map attributes, Map allObjects) {
        super(objectName)
        updateImage(attributes.remove('image'))

        attributes.remove('objects')?.each { objectOrName ->
            def object = allObjects[objectOrName] ?: objectOrName
            if (object instanceof GameObject) {
                objects.put(object.objectName, object)
            }
        }
        setProperty('objects', objects)

        attributes.each { key, value ->
            this.setProperty(key, value)
        }
    }

    final def put(object) {
        objects.put(object.objectName, object)
    }

    final def take(objectName) {
        objects.remove(objectName)
    }

    final def clear() {
        objects.clear()
    }

    final def leftShift(object) {
        put(object)
    }

    @Override
    public void paint(GraphicsContext gc) {
        super.paint(gc)
        objects.each { k,v ->
            v.paint(gc)
        }
    }

}

