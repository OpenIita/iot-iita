package cc.iotkit.data.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.data.cache.ThingModelCacheEvict;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("thingModelDataCache")
public class ThingModelDataCache implements IThingModelData {

    @Autowired
    private IThingModelData thingModelData;
    @Autowired
    private ThingModelCacheEvict thingModelCacheEvict;

    @Override
    @Cacheable(value = Constants.CACHE_THING_MODEL, key = "#root.method.name+#s", unless = "#result == null")
    public ThingModel findById(String s) {
        return thingModelData.findById(s);
    }

    @Override
    public ThingModel save(ThingModel data) {
        data = thingModelData.save(data);
        thingModelCacheEvict.findById(data.getId());
        return data;
    }

    @Override
    public ThingModel add(ThingModel data) {
        return thingModelData.add(data);
    }

    @Override
    public void deleteById(String s) {
        thingModelData.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return thingModelData.count();
    }

    @Override
    public List<ThingModel> findAll() {
        return thingModelData.findAll();
    }

    @Override
    public Paging<ThingModel> findAll(int page, int size) {
        return thingModelData.findAll(page, size);
    }

}
