package data.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class UserEntity {

    private long id;
    private Long worldId;
}
