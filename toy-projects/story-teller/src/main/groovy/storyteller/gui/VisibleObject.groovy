package storyteller.gui

import javafx.scene.canvas.GraphicsContext
import java.awt.Point
import javafx.scene.image.Image
import javafx.scene.Group
import javafx.scene.shape.Rectangle

// Properties of expando are not thread-safe. When using methods defined here, it's thread safe.
class VisibleObject extends Expando {
    protected final String objectName
    protected volatile Image image
    protected volatile Rectangle positionRectangle = new Rectangle(0,0,0,0)

    public VisibleObject(String objectName) {
        this.objectName = objectName
    }

    protected final def updateImage(imageFile) {
        if (imageFile != null) {
            try {
                image = new Image(imageFile)
            } catch (Exception e) {
                println 'Cannot read image ' + imageFile
                e.printStackTrace()
            } finally {
                updatePosition()
            }
        }
    }

    protected final def createRectangle(x, y, width, height) {
        positionRectangle = new Rectangle(x, y, width, height)
    }

    protected final def updatePosition(Map positionFromArgs) {
        def tmpImage = image
        createRectangle(
            positionFromArgs?.get('x') ?: 0,
            positionFromArgs?.get('y') ?: 0,
            tmpImage?.getWidth() ?: 0,
            tmpImage?.getHeight() ?: 0
        )
    }

    protected final def updatePosition(List positionFromArgs) {
        def tmpImage = image
        createRectangle(
            positionFromArgs?.get(0) ?: 0,
            positionFromArgs?.get(1) ?: 0,
            tmpImage?.getWidth() ?: 0,
            tmpImage?.getHeight() ?: 0
        )
    }

    protected final def updatePosition(Point positionFromArgs) {
        def tmpImage = image
        createRectangle(
            positionFromArgs?.x ?: 0,
            positionFromArgs?.y ?: 0,
            tmpImage?.getWidth() ?: 0,
            tmpImage?.getHeight() ?: 0
        )
    }

    protected final def updatePosition() {
        def position = positionRectangle
        def tmpImage = image
        createRectangle(
            position.x,
            position.y,
            tmpImage?.getWidth() ?: 0,
            tmpImage?.getHeight() ?: 0
        )
    }

    public def final Rectangle rectangle() {
        return new Rectangle(
            positionRectangle.getX(),
            positionRectangle.getY(),
            positionRectangle.getWidth(),
            positionRectangle.getHeight()
        )
    }

    def final Point position() {
        return new Point((int)positionRectangle.getX(), (int)positionRectangle.getY())
    }

    public void paint(GraphicsContext gc) {
        def tmpImage = image
        if (tmpImage != null) {
            Rectangle rect = positionRectangle
            gc.drawImage(tmpImage, rect.x, rect.y)
        } else {
            this.draw(gc)
        }
    }

    @Override
    def String toString() {
        name ?: objectName
    }

}

