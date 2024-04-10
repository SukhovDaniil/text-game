package data.dao.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.entity.WorldEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Singleton
public class WorldDao {

    @Inject
    JdbcTemplate jdbcTemplate;

    private final RowMapper<WorldEntity> rowMapper = new WorldRowMapper();

    public Optional<WorldEntity> get(long id) {
        List<WorldEntity> worlds = jdbcTemplate.query("SELECT * FROM worlds WHERE id = ?", rowMapper, id);
        if (worlds.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(worlds.get(0));
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
