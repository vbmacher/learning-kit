package storyteller.gamemodel

import java.awt.Point
import java.util.concurrent.CopyOnWriteArrayList
import java.awt.Graphics
import java.awt.image.BufferedImage
import javafx.scene.canvas.GraphicsContext
import storyteller.gui.VisibleObject

// Not thread-safe
class GameObject extends VisibleObject {

    GameObject(objectName) {
        super(objectName)
    }

    GameObject(objectName, Map attributes) {
        super(objectName)
        updateImage(attributes.remove("image"));
        updatePosition(attributes.remove("position"));
        attributes.each() { key, value ->
            this.setProperty(key, value)
        }
    }

    def move(Point newPosition) {
        updatePosition(newPosition)
    }

    final def changeImage(imageFile) {
        updateImage(imageFile)
        updatePosition()
    }

}

