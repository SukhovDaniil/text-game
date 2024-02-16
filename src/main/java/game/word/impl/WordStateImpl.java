package game.word.impl;

import game.word.Person;
import game.word.Position;
import game.word.Word;

public class WordStateImpl implements Word {

    @Override
    public Position getPosition(Person person) {
        return null;
    }

    @Override
    public WordStateImpl getState() {
        return this;
    }
}
