package data;

import data.entity.UserEntity;
import java.util.Optional;

public interface UserController {

    UserEntity getOrCreate(long userId);

    void delete(long userId);

    Optional<UserEntity> get(long userId);

    void update(UserEntity user);

}
