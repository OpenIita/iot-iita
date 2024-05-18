package cc.iotkit.modbus.data;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.modbus.ModbusInfo;


/**
 * @Description: ModbusInfo数据接口
 * @Author: ZOUZDC
 * @Date: 2024/4/29 0:10
 */
public interface IModbusInfoData extends ICommonData<ModbusInfo, Long> {
    ModbusInfo findByProductKey(String productKey);
}
