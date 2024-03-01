package game.dialog;

import game.npc.Human;

public interface Dialog<S extends Human> {

    Question<S, ?> iterate();

    boolean isFinish();

    default boolean notFinish() {
        return !isFinish();
    }

}
