package data.impl;

import com.google.inject.Inject;
import data.GameController;
import data.UserController;
import data.WorldController;
import data.entity.UserEntity;
import game.Actor;
import game.word.World;
import game.word.impl.PersonImpl;
import game.word.impl.WorldImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameControllerImpl implements GameController {

    @Inject
    UserController userController;

    @Inject
    WorldController worldController;

    @Override
    public void deleteUser(long userId) {
        Optional<UserEntity> userEntity = userController.get(userId);
        userEntity.ifPresent(entity -> {
            log.debug("User [{}] will be deleted", entity.id());
            userController.delete(entity.id());
            if (entity.worldId() != null) {
                log.debug("User`s [{}] world [{}] will be created", entity.id(), entity.worldId());
                worldController.delete(entity.worldId());
            }
        });
    }

    @Override
    public Actor getActorForUser(long userId) {
        UserEntity user = userController.getOrCreate(userId);
        World world;
        if (user.worldId() != null) {
            world = worldController.get(user.worldId());
        } else {
            world = new WorldImpl(100, 100);
            long worldId = worldController.save(world);
            user.worldId(worldId);
            userController.update(user);
        }

        return new Actor(new PersonImpl(), world);
    }

}
