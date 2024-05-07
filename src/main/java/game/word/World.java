package game.word;

import game.interaction.Actionable;
import game.interaction.move.Move;
import game.word.impl.Position;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface World {

    Set<Actionable> getSurroundingActionable(Positionable positionable);

    Set<Move> getPossibleMove(Positionable positionable);

    boolean possibleSetTo(Position position);

    int getXSize();

    int getYSize();

    void setOnPosition(@NotNull Positionable positionable, Position position);

    Set<Positionable> getPositionable();

}
