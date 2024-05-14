package cc.iotkit.modbus.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.modbus.dto.bo.modbus.ModbusInfoBo;
import cc.iotkit.modbus.dto.bo.modbus.ModbusThingModelBo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusInfoVo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusThingModelVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: Modbus模版管理
 * @Author: ZOUZDC
 * @Date: 2024/4/29 0:11
 */
public interface IModbusInfoService {
    Paging<ModbusInfoVo> selectPageList(PageRequest<ModbusInfoBo> request);

    ModbusInfoVo addEntity(ModbusInfoBo data);

    boolean updateEntity(ModbusInfoBo data);

    ModbusInfoVo getDetail(Long data);

    boolean deleteModbus(Long data);

    ModbusThingModelVo getThingModelByProductKey(String data);

    boolean saveThingModel(ModbusThingModelBo data);

    boolean syncToProduct(ModbusThingModelBo data);

    String importData(MultipartFile file, String productKey);
}
