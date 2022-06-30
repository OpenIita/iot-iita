package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.model.space.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class SpaceCache {

    @Autowired
    private SpaceRepository spaceRepository;

    private static SpaceCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static SpaceCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.SPACE_CACHE, key = "#spaceId")
    public Space getSpace(String spaceId) {
        return spaceRepository.findById(spaceId).orElse(null);
    }

}
