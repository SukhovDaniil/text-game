package data.dao.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.entity.WorldEntity;
import org.springframework.jdbc.core.JdbcTemplate;

@Singleton
public class WorldDao {

    @Inject
    JdbcTemplate jdbcTemplate;

    public WorldEntity get(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM worlds WHERE id = ?", new WorldRowMapper(), id);
    }

    public long create(WorldEntity entity) {
        return jdbcTemplate.update("INSERT INTO worlds (x_size, y_size) VALUES (?, ?)", entity.getXSize(),
            entity.getYSize());
    }

    public void delete(long worldId) {
        jdbcTemplate.update("DELETE FROM worlds WHERE id = ?", worldId);
    }

    public long update(WorldEntity entity, long id) {
        return jdbcTemplate.update("UPDATE worlds SET x_size = ?, y_size = ? WHERE id = ?", entity.getXSize(),
            entity.getYSize(), id);
    }

}
