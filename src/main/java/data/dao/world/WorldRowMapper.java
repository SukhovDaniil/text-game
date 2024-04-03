package data.dao.world;

import data.entity.WorldEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class WorldRowMapper implements RowMapper<WorldEntity> {

    @Override
    public WorldEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        WorldEntity entity = new WorldEntity();
        entity.setId(resultSet.getLong("id"));
        entity.setXSize(resultSet.getInt("x_size"));
        entity.setYSize(resultSet.getInt("y_size"));
        return entity;
    }
}
