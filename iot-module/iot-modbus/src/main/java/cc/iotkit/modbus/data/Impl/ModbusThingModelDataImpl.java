package cc.iotkit.modbus.data.Impl;

import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.modbus.dao.ModbusThingModelRepository;
import cc.iotkit.modbus.data.IModbusThingModelData;
import cc.iotkit.modbus.model.TbModbusThingModel;
import cc.iotkit.model.modbus.ModbusThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Description: ModbusInfoDataImpl
 * @Author: ZOUZDC
 * @Date: 2024/5/9 23:36
 */
@Primary
@Service
public class ModbusThingModelDataImpl implements IModbusThingModelData, IJPACommData<ModbusThingModel, Long> {

    @Autowired
    private ModbusThingModelRepository modbusThingModelRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return modbusThingModelRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbModbusThingModel.class;
    }

    @Override
    public Class getTClass() {
        return ModbusThingModel.class;
    }


    @Override
    public ModbusThingModel findByProductKey(String productKey) {
        TbModbusThingModel tbThingModel = modbusThingModelRepository.findByProductKey(productKey);
        ModbusThingModel convert = MapstructUtils.convert(tbThingModel, ModbusThingModel.class);
        if (tbThingModel != null && convert != null) {
            convert.setModel(JsonUtils.parseObject(tbThingModel.getModel(), ModbusThingModel.Model.class));
        }
        return convert;

    }

    @Override
    public ModbusThingModel save(ModbusThingModel data) {
        TbModbusThingModel to = data.to(TbModbusThingModel.class);
        to.setModel(JsonUtils.toJsonString(data.getModel()));
        modbusThingModelRepository.save(to);
        return data;
    }
}
