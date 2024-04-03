package data.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.WorldController;
import data.dao.world.WorldDao;
import data.entity.WorldEntity;
import game.word.World;

@Singleton
public class WorldControllerImpl implements WorldController {

    @Inject
    WorldDao worldDao;

    @Override
    public long save(World world, long id) {
        return worldDao.update(WorldEntity.from(world), id);
    }

    @Override
    public long save(World world) {
        return worldDao.create(WorldEntity.from(world));
    }

    @Override
    public World get(long id) {
        return worldDao.get(id).toWord();
    }

    @Override
    public void delete(long worldId) {
        worldDao.delete(worldId);
    }
}
