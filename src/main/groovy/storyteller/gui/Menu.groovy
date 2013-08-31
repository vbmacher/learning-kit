package storyteller.gui

import java.awt.Point
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.ListView
import javafx.scene.text.Font
import javafx.scene.layout.StackPane
import javafx.scene.Node;
import javafx.scene.input.MouseEvent
import javafx.scene.control.ListCell
import javafx.scene.control.Label
import javafx.util.Callback
import javafx.scene.text.Text

public class Menu extends ListView<MenuOption> {
    private final List<MenuOption> menuOptions = [];
    private final Font menuFont;

    private class MenuOption {
        private final Closure action
        private final String name
        private final BooleanProperty selected
        private final Node node
        private def textWidth
        private def textHeight

        public MenuOption(optionName, action) {
            this.action = action
            this.name = optionName
            this.selected = new SimpleBooleanProperty(this, "selected");
            node = createNode()
        }

        private Node createNode() {
            StackPane node = new StackPane()
            node.setFocusTraversable(true);
            installEventHandler(node);

            Text text = new Text(name)
            text.setFont(menuFont);
            text.snapshot(null, null)
            def layoutBounds = text.getLayoutBounds()
            textWidth = layoutBounds.getWidth();
            textHeight = layoutBounds.getHeight();

            Label nodeLabel = new Label(name);
            nodeLabel.setFont(menuFont);
            nodeLabel.textFillProperty().bind(
                Bindings
                .when(selected)
                .then(DialogComponent.SELECTED_FOREGROUND_COLOR)
                .otherwise(DialogComponent.UNSELECTED_FOREGROUND_COLOR))

            node.getChildren().add(nodeLabel);

            return node
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

    Menu(Map options, Point basePosition) {
        menuFont = DialogComponent.getDefaultFont();
        int width = 0
        int height = 0
        options.each { name, action ->
            def option = new MenuOption(name, action)
            menuOptions << option
            if (option.getTextWidth() > width) {
                width = option.getTextWidth()
            }
            if (height == 0) {
                height = option.getTextHeight()
            }
        }
        def observableOptions = FXCollections.observableList(menuOptions);
        this.setItems(observableOptions)
        this.relocate(basePosition.x, basePosition.y)
        this.setPrefWidth(width + 30);
        this.setPrefHeight(options.size() * (height + 20))

        this.setCellFactory(new Callback<ListView<MenuOption>, ListCell<MenuOption>>(){
                @Override
                public ListCell<MenuOption> call(ListView<MenuOption> listView) {
                    ListCell<MenuOption> cell = new ListCell<MenuOption>(){

                        @Override
                        protected void updateItem(MenuOption option, boolean bln) {
                            super.updateItem(option, bln);
                            if (option != null) {
                                this.setGraphic(option.getNode())
                            }
                        }

                    };
                    return cell;
                }
            })
    }

}

