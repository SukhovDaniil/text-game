package data.dao.users;

import data.entity.UserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<UserEntity> {

    @Override
    public UserEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserEntity()
            .id(resultSet.getLong("id"))
            .worldId(resultSet.getObject("world_id", Long.class));
    }
}
