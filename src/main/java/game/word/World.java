package game.word;

import game.word.impl.Position;

public interface World {

    boolean possibleToMove(Position position);

    int getXSize();

    int getYSize();

}
