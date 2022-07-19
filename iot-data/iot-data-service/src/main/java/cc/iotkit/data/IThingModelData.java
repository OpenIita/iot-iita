package cc.iotkit.data;

import cc.iotkit.model.product.ThingModel;

public interface IThingModelData extends ICommonData<ThingModel, String> {

    ThingModel findByProductKey(String productKey);

}
