/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comps.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.DevicePropertyCache;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import cc.iotkit.temporal.IDevicePropertyData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备属性消息消费入库
 */
@Slf4j
@Service
public class DevicePropertyConsumer implements ConsumerHandler<ThingModelMessage> {

    @Autowired
    private MqConsumer<ThingModelMessage> thingModelMessageMqConsumer;
    @Autowired
    private IDevicePropertyData devicePropertyData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    @Qualifier("thingModelDataCache")
    private IThingModelData thingModelData;

    @PostConstruct
    public void init() {
        thingModelMessageMqConsumer.consume(Constants.DEVICE_PROPERTY_REPORT_TOPIC, this);
    }

    @Override
    public void handler(ThingModelMessage msg) {
        if (!(msg.getData() instanceof Map)) {
            return;
        }

        Map<String, Object> properties = (Map<String, Object>) msg.getData();
        String deviceId = msg.getDeviceId();
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        if (deviceInfo == null) {
            return;
        }

        //物模型校验，过滤非物模型属性
        ThingModel thingModel = thingModelData.findByProductKey(deviceInfo.getProductKey());
        if (thingModel == null) {
            return;
        }

        //物模型属性
        Map<String, ThingModel.DataType> thingModelProperties = thingModel.getModel().
                getProperties().stream().collect(Collectors.toMap(
                ThingModel.Property::getIdentifier, ThingModel.Property::getDataType));

        Map<String, Object> addProperties = new HashMap<>();
        Long occurred = msg.getOccurred();
        //删除非属性字段
        properties.forEach((key,val)->{
            if (thingModelProperties.containsKey(key)) {
                DevicePropertyCache propertyCache = new DevicePropertyCache();
                propertyCache.setValue(val);
                propertyCache.setOccurred(occurred);
                addProperties.put(key,propertyCache);
                handleLocate(deviceInfo,val,thingModelProperties.get(key));
            }
        });

        //更新设备当前属性
        updateDeviceCurrentProperties(deviceId, addProperties);

        //保存属性记录
        try {
            devicePropertyData.addProperties(deviceId, addProperties, occurred);
        } catch (Throwable e) {
            log.warn("save property data error", e);
        }
    }


    private  void handleLocate(DeviceInfo deviceInfo,Object data,ThingModel.DataType dataType){
        if("position".equals(dataType.getType())){//如果是定位属性需要做一些处理
            Object specs = dataType.getSpecs();
            String locateType="";
            if (specs instanceof Map) {
                Object objlocateType = ((Map<?, ?>) specs).get("locateType");//定位方式
                if (objlocateType != null) {
                    locateType = objlocateType.toString();
                }
                if(StringUtils.isNotBlank(locateType)){
                    if("lonLat".equals(locateType)){//经纬度定位格式：经度,纬度
                        String[] lonLats=data.toString().split(",");
                        deviceInfo.getLocate().setLongitude(lonLats[0]);
                        deviceInfo.getLocate().setLatitude(lonLats[1]);
                        deviceInfoData.save(deviceInfo);
                    }else if("basestation".equals(locateType)){//基站定位

                    }else if("ipinfo".equals(locateType)){//ip定位

                    }
                }
            }
        }
    }

    /**
     * 更新设备当前属性
     */
    private void updateDeviceCurrentProperties(String deviceId, Map<String, Object> properties) {
        try {
            log.info("save device property,deviceId:{},property:{}", deviceId, JsonUtils.toJsonString(properties));
            deviceInfoData.saveProperties(deviceId, properties);
        } catch (Throwable e) {
            log.error("save device current properties error", e);
        }
    }
}
