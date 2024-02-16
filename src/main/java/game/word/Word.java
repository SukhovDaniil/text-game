package game.word;

import game.word.impl.WordStateImpl;

public interface Word {
        public Position getPosition(Person person);
        public WordStateImpl getState();
}
