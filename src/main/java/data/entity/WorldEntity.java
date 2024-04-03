package data.entity;

import game.word.World;
import game.word.impl.WorldImpl;
import lombok.Data;

@Data
public class WorldEntity {

    private long id;
    private int xSize;
    private int ySize;

    public static WorldEntity from(World world) {
        WorldEntity entity = new WorldEntity();
        entity.setXSize(world.getXSize());
        entity.setYSize(world.getYSize());
        return entity;
    }

    public static WorldEntity from(World world, long id) {
        WorldEntity entity = from(world);
        entity.setId(id);
        return entity;
    }

    public World toWord() {
        return new WorldImpl(xSize, ySize);
    }

}
