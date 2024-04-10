package game.word;

import game.interaction.move.Move;
import game.word.impl.Position;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface World {

    Set<Move> getPossibleMove(Positionable positionable);

    boolean possibleSetTo(Position position);

    int getXSize();

    int getYSize();

    public void setOnPosition(@NotNull Positionable positionable, Position position);

    public Set<Positionable> getPositionable();

}
