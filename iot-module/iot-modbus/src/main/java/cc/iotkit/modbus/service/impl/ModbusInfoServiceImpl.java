package cc.iotkit.modbus.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.manager.dto.bo.product.ProductBo;
import cc.iotkit.manager.dto.bo.thingmodel.ThingModelBo;
import cc.iotkit.manager.service.IProductService;
import cc.iotkit.modbus.data.IModbusInfoData;
import cc.iotkit.modbus.data.IModbusThingModelData;
import cc.iotkit.modbus.dto.bo.modbus.ModbusInfoBo;
import cc.iotkit.modbus.dto.bo.modbus.ModbusThingModelBo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusInfoVo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusThingModelImportVo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusThingModelVo;
import cc.iotkit.modbus.service.IModbusInfoService;
import cc.iotkit.model.modbus.ModbusInfo;
import cc.iotkit.model.modbus.ModbusThingModel;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.github.yitter.idgen.YitIdHelper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: Modbus模版管理
 * @Author: ZOUZDC
 * @Date: 2024/4/29 0:12
 */
@Service
public class ModbusInfoServiceImpl implements IModbusInfoService {


    @Autowired
    private IModbusInfoData modbusInfoData;
    @Autowired
    private IModbusThingModelData modbusThingModelData;

    /**
     * 如果需要可以直接复写方法,不依赖iot-manager
     */
    @Autowired
    private IProductService productService;

    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;
    @Override
    public Paging<ModbusInfoVo> selectPageList(PageRequest<ModbusInfoBo> request) {
        return modbusInfoData.findAll(request.to(ModbusInfo.class)).to(ModbusInfoVo.class);
    }

    @Override
    @Transactional
    public ModbusInfoVo addEntity(ModbusInfoBo data) {
        ModbusInfo modbusInfo = data.to(ModbusInfo.class);

        ModbusInfo condition = new ModbusInfo();
        condition.setName(data.getName());
        //模板名称不可重复
        ModbusInfo old = modbusInfoData.findOneByCondition(condition);
        if (old != null) {
            throw new BizException(ErrCode.TEMPLATE_NAME_ALREADY);
        }
        //productKey在产品中不可重复
        Product oldProduct  = productData.findByProductKey(data.getProductKey());
        if (oldProduct != null) {
            throw new BizException(ErrCode.PRODUCT_KEY_EXIST);
        }
        //productKey在物模型中不可重复
        old = modbusInfoData.findByProductKey(data.getProductKey());
        if (old != null) {
            throw new BizException(ErrCode.PRODUCT_KEY_EXIST);
        }

        modbusInfo.setCreateAt(System.currentTimeMillis());
        modbusInfo.setUpdateAt(modbusInfo.getCreateAt());
        modbusInfo.setUid(AuthUtil.getUserId());

        modbusInfoData.save(modbusInfo);


        //生成对应的product
        ProductBo productBo = new ProductBo();
        productBo.setProductKey(data.getProductKey());
        productBo.setName(data.getName());
        /**
         * [{value:0,label:'网关设备',},{value:1,label:'网关子设备',},{value:2,label:'直连设备'},]
         */
        productBo.setNodeType(1);
        productBo.setTransparent(true);
        productBo.setKeepAliveTime(0L);
        productBo.setIsOpenLocate(false);
        productService.addEntity(productBo);

        return MapstructUtils.convert(modbusInfo, ModbusInfoVo.class);
    }

    @Override
    public boolean updateEntity(ModbusInfoBo data) {
        ModbusInfo modbusInfo = data.to(ModbusInfo.class);

        ModbusInfo condition = new ModbusInfo();
        condition.setName(data.getName());
        //模板名称不可重复
        ModbusInfo old = modbusInfoData.findOneByCondition(condition);
        if (old != null && !Objects.equals(data.getId(), old.getId())) {
            throw new BizException(ErrCode.TEMPLATE_NAME_ALREADY);
        }

        modbusInfo.setUpdateAt(System.currentTimeMillis());

        modbusInfoData.save(modbusInfo);
        return true;
    }

    @Override
    public ModbusInfoVo getDetail(Long id) {
        return MapstructUtils.convert(modbusInfoData.findById(id), ModbusInfoVo.class);
    }

    @Override
    public boolean deleteModbus(Long id) {

        ModbusInfo info = modbusInfoData.findById(id);

        Product oldProduct  = productData.findByProductKey(info.getProductKey());
        if (oldProduct != null) {
            //存在关联产品
            throw new BizException(ErrCode.RELATED_PRODUCTS_EXIST);
        }

        modbusInfoData.deleteById(id);
        return true;
    }

    @Override
    public ModbusThingModelVo getThingModelByProductKey(String productKey) {
        ModbusThingModel thingModel = modbusThingModelData.findByProductKey(productKey);
        return MapstructUtils.convert(thingModel, ModbusThingModelVo.class);
    }

    @Override
    @Transactional
    public boolean saveThingModel(ModbusThingModelBo data) {
        String productKey = data.getProductKey();

        ModbusThingModel thingModel = new ModbusThingModel(YitIdHelper.nextId(), productKey, JsonUtils.parseObject(data.getModel(), ModbusThingModel.Model.class),System.currentTimeMillis());
        return saveThingModel(productKey,thingModel);
    }
    public boolean saveThingModel(String productKey, ModbusThingModel thingModel) {

        ModbusThingModel oldTm = modbusThingModelData.findByProductKey(productKey);

        //验证物模型合法性
        List<ModbusThingModel.Property> properties = thingModel.getModel().getProperties();
        for (ModbusThingModel.Property property : properties) {
            //属性标识符合法性校验
            String identifier = property.getIdentifier();
            if (StringUtils.isBlank(identifier)) {
                throw new BizException("属性标识符不能为空");
            }
            if (!identifier.matches("^[a-zA-Z].*")) {
                throw new BizException("属性标识符【" + identifier + "】不合法");
            }
        }
        if(oldTm!=null){
            thingModel.setId(oldTm.getId());
        }
        modbusThingModelData.save(thingModel);
        return true;
    }

    @Override
    public boolean syncToProduct(ModbusThingModelBo data) {
        String productKey = data.getProductKey();

        Product product  = productData.findByProductKey(productKey);
        if (product == null) {
            throw new BizException(ErrCode.PRODUCT_NOT_FOUND);
        }

        ModbusThingModel modbusThingModel = modbusThingModelData.findByProductKey(productKey);


        ThingModel.Model model = new ThingModel.Model();

        //将Property私有属性封装到proData中
        List<ThingModel.Property> properties=new ArrayList<>();

        if(CollectionUtil.isNotEmpty(modbusThingModel.getModel().getProperties())){
            properties = modbusThingModel.getModel().getProperties().stream().map(p -> {
                ThingModel.Property property = MapstructUtils.convert(p, ThingModel.Property.class);
                property.setProData(JSONUtil.toJsonStr(MapstructUtils.convert(p, ModbusThingModel.Property.class)));
                return property;
            }).collect(Collectors.toList());
        }
        model.setProperties(properties);

        //services events 数据封装



        ThingModelBo tbspModel = new ThingModelBo();
        tbspModel.setProductKey(productKey);
        tbspModel.setModel(JSONUtil.toJsonStr(model));


        productService.saveThingModel(tbspModel);


        return true;
    }

    @SneakyThrows
    @Override
    public String importData(MultipartFile file, String productKey) {

        List<ModbusThingModelImportVo> objects = EasyExcel.read(file.getInputStream()).head(ModbusThingModelImportVo.class).sheet().doReadSync();
        if(CollectionUtil.isEmpty(objects)){
            throw new BizException(ErrCode.DATA_NOT_EXIST);
        }
        List<ModbusThingModel.Property> modbusThingModelArrayList = new ArrayList<>();
        for (ModbusThingModelImportVo vo : objects) {
            ModbusThingModel.Property convert = MapstructUtils.convert(vo, ModbusThingModel.Property.class);
            modbusThingModelArrayList.add(convert);
        }

        ModbusThingModel.Model model = new ModbusThingModel.Model();
        model.setProperties(modbusThingModelArrayList);

        ModbusThingModel modbusThingModel = new ModbusThingModel();
        modbusThingModel.setModel(model );


        return this.saveThingModel(productKey, modbusThingModel)?"导入成功":"导入失败";


    }


}
