package data;

import game.Actor;

public interface GameController {

    void initUser(long userId);

    void deleteUser(long userId);

    Actor getActorForUser(long userId);

    void save(Actor actor, long userId);

}
