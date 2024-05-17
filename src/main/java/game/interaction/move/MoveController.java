package game.interaction.move;

import game.word.Positionable;
import game.word.impl.Position;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MoveController {

    public void move(Positionable positionable, Move move) {
        Position old = positionable.getPosition();
        Position newPosition = old.shift(move);
        positionable.setPosition(newPosition);
        log.debug("[{}] {} [{}] сдвинулся на {}: с {} на {}", Thread.currentThread(), positionable,
            positionable.hashCode(), move.getDirection().getName().toLowerCase(), old, newPosition);
    }
}
