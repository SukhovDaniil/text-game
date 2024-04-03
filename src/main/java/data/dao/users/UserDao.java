package data.dao.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.springframework.jdbc.core.JdbcTemplate;

@Singleton
public class UserDao {

    @Inject
    JdbcTemplate jdbcTemplate;

    public boolean isExist(long id) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM users WHERE id=?", Integer.class, id) > 0;
    }

    public void create(long id) {
        jdbcTemplate.update("INSERT INTO users (id) VALUES (?)", id);
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
    }

}
