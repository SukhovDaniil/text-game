package data.dao.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Singleton
public class UserDao {

    @Inject
    JdbcTemplate jdbcTemplate;

    private final RowMapper<UserEntity> rowMapper = new UserRowMapper();

    public Optional<UserEntity> get(long userId) {
        List<UserEntity> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", rowMapper, userId);
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    public void create(UserEntity entity) {
        jdbcTemplate.update("INSERT INTO users (id, world_id) VALUES (?, ?)", entity.id(), entity.worldId());
    }

    public void update(UserEntity entity) {
        jdbcTemplate.update("UPDATE users SET world_id = ? WHERE id = ?", entity.worldId(), entity.id());
    }

    public void delete(UserEntity entity) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", entity.id());
    }

}
