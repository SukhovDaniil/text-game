package data;

import game.word.World;

public interface WorldProvider {

    long save(World world, long id);

    long save(World world);

    World get(long id);

    void delete(long worldId);

}
