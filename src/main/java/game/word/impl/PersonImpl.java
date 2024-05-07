package game.word.impl;

import game.word.Person;
import game.word.Positionable;

public class PersonImpl implements Person, Positionable {

    private Position position = new Position(0, 0);
    private boolean isBusy = false;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Position setPosition(Position position) {
        return this.position = position;
    }

    @Override
    public String toString() {
        return "Персонаж";
    }

    @Override
    public boolean isBusy() {
        return isBusy;
    }

    @Override
    public void occupy() {
        isBusy = true;
    }

    @Override
    public void toFree() {
        isBusy = false;
    }
}
