package storyteller.gui

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.Node;
import javafx.event.EventHandler
import javafx.beans.binding.Bindings

public class Option {
    private final Closure action
    private final String name
    private final BooleanProperty selected
    private final Node node
    private def textWidth
    private def textHeight

    public Option(optionName, action) {
        this.action = action
        this.name = optionName
        this.selected = new SimpleBooleanProperty(this, "selected");
        node = createNode()
    }

    private Node createNode() {
        StackPane node = new StackPane()
        node.setFocusTraversable(true);
        installEventHandler(node);

        calculateTextSize()
        Label nodeLabel = new Label(name);
        nodeLabel.setFont(Defaults.getDefaultFont());
        nodeLabel.textFillProperty().bind(
            Bindings
            .when(selected)
            .then(Defaults.SELECTED_FOREGROUND_COLOR)
            .otherwise(Defaults.UNSELECTED_FOREGROUND_COLOR))
        node.getChildren().add(nodeLabel);
        return node
    }

    private void calculateTextSize() {
        Text text = new Text(name)
        text.setFont(Defaults.getDefaultFont());
        text.snapshot(null, null)
        def layoutBounds = text.getLayoutBounds()
        textWidth = layoutBounds.getWidth();
        textHeight = layoutBounds.getHeight();
    }

    def getTextWidth() { return textWidth }
    def getTextHeight() { return textHeight }

    public Node getNode() {
        return node
    }

    private void installEventHandler(Node node) {
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent mouseEvent) {
                    mouseEvent.consume();
                    action()
                }
            })
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent mouseEvent) {
                    mouseEvent.consume();
                    selected.set(true)
                }
            })
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent mouseEvent) {
                    mouseEvent.consume();
                    selected.set(false)
                }
            })
    }
}
