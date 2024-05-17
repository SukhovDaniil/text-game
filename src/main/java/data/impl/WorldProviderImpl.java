package data.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import data.WorldProvider;
import data.dao.world.WorldDao;
import data.entity.WorldEntity;
import game.word.World;
import game.word.impl.WorldImpl;

@Singleton
public class WorldProviderImpl implements WorldProvider {

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
        WorldEntity entity = worldDao.get(id).get();
        return new WorldImpl(entity.getXSize(), entity.getYSize());
    }

    @Override
    public void delete(long worldId) {
        worldDao.delete(worldId);
    }
}
