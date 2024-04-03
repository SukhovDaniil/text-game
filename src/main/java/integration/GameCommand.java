package integration;

import java.util.Arrays;
import lombok.Getter;

public enum GameCommand {
    NEW_USER("/create"),
    DELETE_USER("/delete"),
    START("/start"),
    EXIT("/exit"),
    SAVE("/save");

    @Getter
    private final String value;

    GameCommand(String value) {
        this.value = value;
    }

    public static boolean isGameCommand(String value) {
        return Arrays.stream(values()).anyMatch(c -> c.getValue().equalsIgnoreCase(value));
    }
}
