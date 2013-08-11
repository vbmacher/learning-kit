package storyteller.menu

import storyteller.GameObject
import java.awt.Point
import java.awt.Graphics

class Label extends GameObject {
    private final Point position
    private final def name

    public Label(Point position, name) {
        super('label' + name)
        this.position = position
        this.name = name
    }

    def draw(Graphics graphics) {
        graphics.setColor(DialogComponent.UNSELECTED_FOREGROUND_COLOR)
        graphics.drawString(name, (int)position.x, (int)position.y)
    }

}

