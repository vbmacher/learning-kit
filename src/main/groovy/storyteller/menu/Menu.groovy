package storyteller.menu

import java.awt.Point
import java.awt.Graphics
import java.awt.Font
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage
import javax.swing.JComponent
import java.awt.event.MouseMotionListener
import java.awt.Rectangle
import storyteller.menu.DialogComponent
import storyteller.GameObject

public class Menu extends GameObject {
    private final static int VERTICAL_GAP = 10
    private final static int HORIZONTAL_GAP = 5

    private final Rectangle menuRectangle
    private final int textHeight

    private final def menuFont;

    private final menuOptions = []
    private MenuOption selectedOption

    private class MenuOption {
        private final Closure action
        private final Rectangle rectangle
        private final def name
        private volatile Color color = DialogComponent.UNSELECTED_FOREGROUND_COLOR;

        public MenuOption(optionName, optionRect, action) {
            this.action = action
            this.name = optionName
            this.rectangle = optionRect
        }

        def draw(Graphics graphics) {
            graphics.setColor(color)
            graphics.drawString(name, (int)rectangle.x, textHeight + (int)rectangle.y)
        }

        def getRectangle() {
            return rectangle
        }

        def doAction() {
            action()
        }

        def select() {
            color = DialogComponent.SELECTED_FOREGROUND_COLOR;
        }

        def unselect() {
            color = DialogComponent.UNSELECTED_FOREGROUND_COLOR;
        }

        def String toString() {
            return "MenuOption ${name} at ${rectangle}"
        }

    }

    Menu(Map options, Point basePosition) {
        super('menu', [ position: basePosition ])

        menuFont = DialogComponent.getDefaultFont();
        def metrics = DialogComponent.getDefaultFontMetrics()

        textHeight = metrics.getHeight()
        def maxWidth = getMaxWidth(metrics, options.keySet())

        Point textPosition = new Point(
            (int)basePosition.x + VERTICAL_GAP,
            (int)basePosition.y + VERTICAL_GAP
        )

        createRectangle(
            (int)basePosition.x,
            (int)basePosition.y,
            maxWidth + 4 * HORIZONTAL_GAP,
            VERTICAL_GAP + (textHeight + VERTICAL_GAP) * options.size() + 2*VERTICAL_GAP
        )

        options.eachWithIndex { menuOption, i ->
            menuOptions << new MenuOption(
                menuOption.key,
                new Rectangle(
                    (int)textPosition.x,
                    (int)textPosition.y + (textHeight + 2*VERTICAL_GAP) * i,
                    (int)positionRectangle.width,
                    textHeight
                ),
                menuOption.value
            )
        }
    }

    private int getMaxWidth(metrics, text) {
        def maxWidth = 0
        text.each {
            def w = metrics.stringWidth(it);
            if (w > maxWidth) {
                maxWidth = w
            }
        }
        return maxWidth
    }

    def draw(Graphics graphics) {
        graphics.setFont(menuFont)
        graphics.setColor(DialogComponent.BACKGROUND_COLOR)
        graphics.fillRect(
            (int)positionRectangle.x,
            (int)positionRectangle.y,
            (int)positionRectangle.width,
            (int)positionRectangle.height
        )
        menuOptions.each {
            it.draw(graphics)
        }
    }

    private MenuOption getOptionAt(Point point) {
        menuOptions.find {
            ((Rectangle)it.getRectangle()).contains(point)
        }
    }

    def clicked(point) {
        getOptionAt(point)?.doAction()
    }

    def mouseMoved(point) {
        def option = getOptionAt(point)

        if (selectedOption == option) {
            return
        }
        selectedOption?.unselect()
        option?.select()
        selectedOption = option
    }

}

