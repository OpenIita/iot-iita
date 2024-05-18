package cc.iotkit.modbus.dao;

import cc.iotkit.modbus.model.TbModbusThingModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: ModbusInfoRepository
 * @Author: ZOUZDC
 * @Date: 2024/4/29 0:11
 */
public interface ModbusThingModelRepository extends JpaRepository<TbModbusThingModel, Long> {

    TbModbusThingModel findByProductKey(String productKey);
}
