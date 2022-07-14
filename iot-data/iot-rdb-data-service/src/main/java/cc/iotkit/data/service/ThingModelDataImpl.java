package cc.iotkit.data.service;

import cc.iotkit.data.IThingModelData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.ThingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThingModelDataImpl implements IThingModelData {
    @Override
    public ThingModel findByProductKey(String productKey) {
        return null;
    }

    @Override
    public ThingModel findById(String s) {
        return null;
    }

    @Override
    public ThingModel save(ThingModel data) {
        return null;
    }

    @Override
    public ThingModel add(ThingModel data) {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<ThingModel> findAll() {
        return null;
    }

    @Override
    public Paging<ThingModel> findAll(int page, int size) {
        return null;
    }
}
