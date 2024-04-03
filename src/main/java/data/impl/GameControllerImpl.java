package data.impl;

import com.google.inject.Inject;
import data.GameController;
import data.UserController;
import data.WorldController;
import game.Actor;
import game.word.World;
import game.word.impl.PersonImpl;
import game.word.impl.WorldImpl;
import java.util.Optional;

public class GameControllerImpl implements GameController {

    @Inject
    UserController userController;

    @Inject
    WorldController worldController;

    @Override
    public void initUser(long userId) {
        userController.initIfNotExist(userId);
    }

    @Override
    public void deleteUser(long userId) {
        Optional<Long> worldId = userController.getUserWorldId(userId);
        userController.deleteIfExist(userId);
        worldId.ifPresent(aLong -> worldController.delete(aLong));
    }

    @Override
    public Actor getActorForUser(long userId) {
        Optional<Long> worldId = userController.getUserWorldId(userId);
        World world;
        if (worldId.isPresent()) {
            world = worldController.get(worldId.get());
        } else {
            world = new WorldImpl(10, 10);
            long newWorldId = worldController.save(world);
            userController.setUserWorldId(userId, newWorldId);
        }
        return new Actor(new PersonImpl(), world);
    }

    @Override
    public void save(Actor actor, long userId) {

    }
}
