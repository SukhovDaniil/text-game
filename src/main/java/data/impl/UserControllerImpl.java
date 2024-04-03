package data.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.UserController;
import data.dao.users.UserDao;
import data.dao.users.UsersWorldDao;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class UserControllerImpl implements UserController {

    @Inject
    UserDao userDao;

    @Inject
    UsersWorldDao usersWorldDao;

    @Override
    public void initIfNotExist(long userId) {
        if (!userDao.isExist(userId)) {
            log.debug("Not exist user with id [%s] - will be created".formatted(userId));
            userDao.create(userId);
        }
    }

    @Override
    public void deleteIfExist(long userId) {
        Optional<Long> worldId = getUserWorldId(userId);
        if (worldId.isPresent()) {
            usersWorldDao.deleteLink(userId);
        }
        if (userDao.isExist(userId)) {
            log.debug("User with id [%s] will be delete");
            userDao.delete(userId);
        }
    }

    @Override
    public Optional<Long> getUserWorldId(long userId) {
        if (usersWorldDao.hasWorld(userId)) {
            return Optional.of(usersWorldDao.getWorldId(userId));
        }
        return Optional.empty();
    }

    @Override
    public void setUserWorldId(long userId, long worldId) {
        usersWorldDao.createLink(userId, worldId);
    }
}
