package data.dao.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.jdbc.core.JdbcTemplate;

@Singleton
public class UsersWorldDao {

    @Inject
    JdbcTemplate jdbcTemplate;

    public boolean hasWorld(long userId) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users_worlds WHERE user_id", Long.class) > 0;
    }

    public long getWorldId(long userId) {
        return jdbcTemplate.queryForObject("SELECT world_id FROM users_worlds WHERE user_id", Long.class);
    }

    public void createLink(long userId, long worldId) {
        jdbcTemplate.update("INSERT INTO users_worlds (user_id, world_id) VALUES  (?, ?)", userId, worldId);
    }

    public void deleteLink(long userId) {
        jdbcTemplate.update("DELETE FROM users_worlds WHERE user_id = ?", userId);
    }

}
