package cc.iotkit.modbus.data.Impl;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.modbus.dao.ModbusInfoRepository;
import cc.iotkit.modbus.data.IModbusInfoData;
import cc.iotkit.modbus.model.TbModbusInfo;
import cc.iotkit.model.modbus.ModbusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Description: ModbusInfoDataImpl
 * @Author: ZOUZDC
 * @Date: 2024/4/29 0:11
 */
@Primary
@Service
public class ModbusInfoDataImpl implements IModbusInfoData, IJPACommData<ModbusInfo, Long> {

    @Autowired
    private ModbusInfoRepository modbusInfoRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return modbusInfoRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbModbusInfo.class;
    }

    @Override
    public Class getTClass() {
        return ModbusInfo.class;
    }


    @Override
    public ModbusInfo findByProductKey(String productKey) {
        return MapstructUtils.convert(modbusInfoRepository.findByProductKey(productKey), ModbusInfo.class);
    }
}
