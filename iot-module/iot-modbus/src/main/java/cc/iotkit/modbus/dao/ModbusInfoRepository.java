package cc.iotkit.modbus.dao;

import cc.iotkit.modbus.model.TbModbusInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: ModbusInfoRepository
 * @Author: ZOUZDC
 * @Date: 2024/4/29 0:11
 */
public interface ModbusInfoRepository extends JpaRepository<TbModbusInfo, Long> {


    TbModbusInfo findByProductKey(String productKey);
}
