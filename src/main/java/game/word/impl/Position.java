package game.word.impl;

import game.interaction.move.Move;
import lombok.Getter;

@Getter
public class Position {

    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position shift(Move move) {
        int xShift = move.getDirection().getXShift() * move.getShift();
        int yShift = move.getDirection().getYShift() * move.getShift();
        return new Position(this.x + xShift, this.y + yShift);
    }

    @Override
    public String toString() {
        return "(%s, %s)".formatted(this.x, this.y);
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
