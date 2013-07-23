package storyteller

import java.awt.Point
import java.util.concurrent.CopyOnWriteArrayList
import java.awt.Graphics

// Thread-safe, only if you use methods inside this class. Expando is not thread-safe.
class GameObject extends Expando {
    private final List<GameObject> children = new CopyOnWriteArrayList<GameObject>();
    def volatile image
    def volatile Point position = new Point(0,0)

    GameObject(objectName) {
        this.objectName = objectName
    }

    GameObject(objectName, Map attributes) {
        this(objectName)
        updatePosition(attributes.remove("position"));
        updateImage(attributes.remove("image"));
        attributes.each() { key, value ->
            this.setProperty(key, value)
        }
    }

    private void updatePosition(positionFromArgs) {
        if (positionFromArgs instanceof Map) {
            Map<String, Integer> mapPosition = (Map<String, Integer>)positionFromArgs;
            position = new Point(mapPosition.get("x"), mapPosition.get("y"));
        } else if (positionFromArgs instanceof List) {
            List<Integer> listPosition = (List<Integer>)positionFromArgs;
            position = new Point(listPosition.get(0), listPosition.get(1));
        }
    }

    private void updateImage(imageFile) {
        try {
            this.image = image(file: imageFile)
        } catch (Throwable e) {
            e.printStackTrace()
        }
    }

    def addChild(GameObject gameObject) {
        children << gameObject
    }

    def removeChild(GameObject gameObject) {
        children.remove(gameObject)
    }

    def leftShift(GameObject gameObject) {
        addChild(gameObject)
    }

    def String toString() {
        "{GameObject=$name,children=$children}"
    }

    def move(Point newPosition) {
        position = newPosition
    }

    def changeImage(imageFile) {
        updateImage(imageFile)
    }

    def void paint(Graphics graphics, Point basePosition) {
        Point newPosition = new Point(basePosition.x + position.x,basePosition.y + position.y)
        graphics.drawImage(image, newPosition.x, newPosition.y, null)
        children.each {
            it.paint(graphics, newPosition)
        }
    }

    def void paint(Graphics graphics) {
        graphics.drawImage(image, position.x, position.y, null)
        children.each {
            it.paint(graphics, position)
        }
    }

}

