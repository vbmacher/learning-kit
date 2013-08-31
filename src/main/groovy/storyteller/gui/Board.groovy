package storyteller.gui

import javax.swing.JPanel
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.Graphics2D
import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.Color
import java.util.concurrent.CountDownLatch
import javafx.scene.canvas.GraphicsContext
import javafx.scene.canvas.Canvas
import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.animation.FadeTransition
import javafx.util.Duration
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler

public class Board extends Pane {
    private volatile RoomComponent currentRoom;

    public final void setCurrentRoom(RoomComponent room) {
        RoomComponent oldRoom = currentRoom
        if (oldRoom == null) {
            setNewRoom(room)
        } else {
            Platform.runLater(new Runnable() {
                    def void run() {
                        FadeTransition fd = createFadeOut(oldRoom)
                        fd.setOnFinished(new EventHandler<ActionEvent>() {

                                public void handle(ActionEvent event) {
                                    getChildren().remove(oldRoom)
                                    setNewRoom(room)
                                }

                            })
                        fd.play()
                    }
                })
        }
    }

    private void setNewRoom(room) {
        if (room != null) {
            room.setOpacity(0.0)
            getChildren().add(room)
            createFadeIn(room).play()
        }
        currentRoom = room
        autosize()
    }

    private FadeTransition createFadeIn(room) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), room);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        return fadeTransition;
    }

    private FadeTransition createFadeOut(room) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), room);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        return fadeTransition;
    }

}