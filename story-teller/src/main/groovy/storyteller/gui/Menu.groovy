package storyteller.gui

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.scene.control.ListView
import javafx.scene.text.Font
import javafx.scene.Node;
import javafx.scene.control.ListCell
import javafx.scene.control.Label
import javafx.util.Callback
import javafx.scene.text.Text

public class Menu extends ListView<Option> {
    private final List<Option> menuOptions = [];

    Menu(Map options, int width, int height) {
        options.each { name, action ->
            def option = new Option(name, action)
            menuOptions << option
        }
        this.setPrefWidth(width)
        this.setPrefHeight(height)
        init()
    }

    Menu(Map options) {
        int width = 0
        int height = 0
        options.each { name, action ->
            def option = new Option(name, action)
            menuOptions << option
            width = (option.getTextWidth() > width) ? option.getTextWidth() : width
            height = height ?: option.getTextHeight()
        }
        this.setPrefWidth(width + 30);
        this.setPrefHeight(options.size() * (height + 20))
        init()
    }

    private void init() {
        def observableOptions = FXCollections.observableList(menuOptions);
        this.setItems(observableOptions)
        this.setCellFactory(new Callback<ListView<Option>, ListCell<Option>>() {
                @Override
                public ListCell<Option> call(ListView<Option> listView) {
                    ListCell<Option> cell = new ListCell<Option>(){

                        @Override
                        protected void updateItem(Option option, boolean bln) {
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

