package storyteller

import java.awt.Point
import java.util.concurrent.CopyOnWriteArrayList
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.Rectangle

// Not thread-safe
class GameObject extends Expando {
    def volatile BufferedImage image
    protected volatile Rectangle positionRectangle = new Rectangle(0,0,0,0)

    GameObject(objectName) {
        this.objectName = objectName
    }

    GameObject(objectName, Map attributes) {
        this(objectName)
        updateImage(attributes.remove("image"));
        updatePosition(attributes.remove("position"));
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
        } else if (positionFromArgs instanceof Point) {
            position = positionFromArgs
        } else {
            position = new Point(0,0)
        }
        createRectangle((int)position.x, (int)position.y, image?.getWidth() ?: 1, image?.getHeight() ?: 1)
    }

    protected final void createRectangle(int x, int y, int width, int height) {
        positionRectangle = new Rectangle(x, y, width, height)
    }

    private void updateImage(imageFile) {
        if (imageFile == null) {
            return
        }
        try {
            this.image = ImageIO.read(imageFile)
        } catch (Throwable e) {
            e.printStackTrace()
        }
    }

    def String toString() {
        "{GameObject=$objectName}"
    }

    def move(Point newPosition) {
        updatePosition(newPosition)
    }

    final def changeImage(imageFile) {
        updateImage(imageFile)
        updatePosition(positionRectangle.getLocation())
    }

    def void paint(Graphics graphics) {
        if (image != null) {
            Rectangle rect = positionRectangle
            graphics.drawImage(image, rect.x, rect.y, null)
        } else {
            this?.draw(graphics)
        }
    }

    def getRectangle() {
        return new Rectangle(positionRectangle)
    }

}

