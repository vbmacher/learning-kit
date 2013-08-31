package storyteller.gui

import javafx.scene.layout.Pane
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.shape.Rectangle
import storyteller.gamemodel.Room

public class RoomComponent extends Pane {
    protected final Room room
    private final Canvas canvas = new Canvas()

    public RoomComponent(Room room) {
        this.room = room
        def rect = room.rectangle()
        this.prefWidthProperty().bind(rect.widthProperty())
        this.prefHeightProperty().bind(rect.heightProperty())

        setCache(true)
        canvas.widthProperty().bind(this.widthProperty())
        canvas.heightProperty().bind(this.heightProperty())
        GraphicsContext gc = canvas.getGraphicsContext2D()
        gc.translate(5, 5);
        room.paint(gc)

        getChildren().add(canvas)
    }

}

