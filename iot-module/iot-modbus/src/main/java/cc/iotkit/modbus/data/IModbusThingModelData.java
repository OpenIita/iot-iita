package cc.iotkit.modbus.data;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.modbus.ModbusThingModel;


/**
 * @Description: ModbusThingModel数据接口
 * @Author: ZOUZDC
 * @Date: 2024/5/9 23:36
 */
public interface IModbusThingModelData extends ICommonData<ModbusThingModel, Long> {

    ModbusThingModel findByProductKey(String productKey);

    ModbusThingModel save(ModbusThingModel thingModel);
}
