package data;

import game.Actor;

public interface GameController {

    void deleteUser(long userId);

    Actor getActorForUser(long userId);

}
