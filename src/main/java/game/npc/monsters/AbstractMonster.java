package game.npc.monsters;

import game.npc.Monster;
import game.word.Positionable;
import game.word.impl.Position;
import lombok.Getter;

@Getter
public abstract class AbstractMonster implements Monster, Positionable {

    private final String name;
    private final long id;
    private Position position;

    protected AbstractMonster(String name, long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public Position setPosition(Position position) {
        this.position = position;
        return this.position;
    }
}
