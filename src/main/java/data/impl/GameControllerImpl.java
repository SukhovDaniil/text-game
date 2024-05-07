package data.impl;

import com.google.inject.Inject;
import data.GameController;
import data.UserController;
import data.WorldController;
import data.entity.UserEntity;
import game.Actor;
import game.items.potion.HealthPotion;
import game.items.weapon.Sword;
import game.npc.humans.Seller;
import game.word.Person;
import game.word.Positionable;
import game.word.World;
import game.word.impl.PersonImpl;
import game.word.impl.Position;
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
        world.setOnPosition(new Seller()
            .addItem(new Sword(10, "меч-леденец", 1))
            .addItem(new HealthPotion("вода жизни", 1000))
            .addItem(new Sword(66, "меч", 69)), new Position(12, 78));

        Person person = new PersonImpl();
        world.setOnPosition(((Positionable) person), new Position(11, 77));
        return new Actor(person, world);
    }

}
