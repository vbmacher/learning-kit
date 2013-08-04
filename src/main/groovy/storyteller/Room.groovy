package storyteller

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.awt.Graphics
import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.imageio.ImageIO;

// Thread-safe only when using methods inside this class. Expando is not thread-safe.
class Room extends Expando {
    def volatile BufferedImage image
    def final objectName
    protected final ConcurrentMap<String,GameObject> objects = new ConcurrentHashMap<String, GameObject>()

    Room(objectName, Map attributes, Map allObjects) {
        this.objectName = objectName
        updateImage(attributes.remove('image'))

        attributes.remove('objects')?.each {
            object = allObjects[it]
            if (object != null) {
                objects.put(it, object)
            }
        }
        setProperty('objects', objects)

        attributes.each { key, value ->
            this.setProperty(key, value)
        }
    }

    def put(object) {
        objects.put(object.objectName, object)
    }

    def take(objectName) {
        objects.remove(objectName)
    }

    def clear() {
        objects.clear()
    }

    protected final void updateImage(imageFile) {
        if (imageFile == null) {
            return
        }
        try {
            this.image = ImageIO.read(imageFile)
        } catch (Throwable e) {
            e.printStackTrace()
        }
    }

    def leftShift(object) {
        insertObject(object)
    }

    def String toString() {
        "{Room=$name}"
    }

    def void paint(Graphics graphics) {
        graphics.drawImage(image, 0, 0, null)
        objects.each { k,v ->
            v.paint(graphics)
        }
    }

    def Dimension getSize() {
        return new Dimension(image.getWidth(), image.getHeight())
    }
}

