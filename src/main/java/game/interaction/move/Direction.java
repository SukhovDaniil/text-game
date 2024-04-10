package game.interaction.move;

import lombok.Getter;

@Getter
public enum Direction {
    EAST("Восток", -1, 0),
    WEST("Запад", 1, 0),
    NORTH("Север", 0, 1),
    SOUTH("Юг", 0, -1);

    private final int xShift;
    private final int yShift;
    private final String name;

    Direction(String name, int x, int y) {
        this.name = name;
        this.xShift = x;
        this.yShift = y;
    }
}
