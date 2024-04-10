package data.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.UserController;
import data.dao.users.UserDao;
import data.entity.UserEntity;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class UserControllerImpl implements UserController {

    @Inject
    UserDao userDao;

    @Override
    public UserEntity getOrCreate(long userId) {
        Optional<UserEntity> checkedUser = userDao.get(userId);
        if (checkedUser.isPresent()) {
            return checkedUser.get();
        }

        UserEntity user = new UserEntity().id(userId);
        userDao.create(user);
        return user;
    }

    @Override
    public void delete(long userId) {
        userDao.delete(new UserEntity().id(userId));
    }

    public Optional<UserEntity> get(long userId) {
        return userDao.get(userId);
    }

    @Override
    public void update(UserEntity user) {
        userDao.update(user);
    }
}
