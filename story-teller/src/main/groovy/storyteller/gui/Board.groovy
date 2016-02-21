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
    private RoomComponent currentRoom; // Thread confinement (FX Thred)

    public final void setCurrentRoom(final RoomComponent room) {
        Platform.runLater(new Runnable() {
                def void run() {
                    setNewRoom(room)
                }
            })
    }

    // Must be called before FX Application thread starts!
    // TODO: applyCurrentRoom call is safe?
    public final void setFirstRoom(room) {
        currentRoom = room
        applyCurrentRoom()
    }

    // Must be called only in FX Application thread!
    private void setNewRoom(room) {
        FadeTransition fd = createFadeOut(currentRoom)
        fd.setOnFinished(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent event) {
                    if (currentRoom != null) {
                        getChildren().remove(currentRoom)
                    }
                    currentRoom = room
                    applyCurrentRoom()
                }
            })
        fd.play()
    }

    // Must be called only in FX Application thread!
    private void applyCurrentRoom() {
        currentRoom.setOpacity(0.0)
        getChildren().add(currentRoom)
        autosize()
        createFadeIn(currentRoom).play()
    }

    private FadeTransition createFadeIn(room) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), room);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        return fadeTransition;
    }

    private FadeTransition createFadeOut(room) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(700), room);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        return fadeTransition;
    }

}