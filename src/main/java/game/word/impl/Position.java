package game.word.impl;

import lombok.Getter;

@Getter
public class Position {

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position shift(int xShift, int yShift) {
        return new Position(this.x + xShift, this.y + yShift);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        return this.x == ((Position) obj).getX() && this.y == ((Position) obj).getY();
    }
}
