package student;

import java.util.Objects;

public class Block {
    public final int length;
    public final String color;

    Block(int length, String color) {
        this.length = length;
        this.color = Objects.requireNonNull(color);
    }
}
