package data;

import java.util.Optional;

public interface UserController {

    void initIfNotExist(long userId);

    void deleteIfExist(long userId);

    Optional<Long> getUserWorldId(long userId);

    void setUserWorldId(long userId, long worldId);

}
