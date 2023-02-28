/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comps;


import cc.iotkit.common.ComponentClassLoader;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IComponent;
import cc.iotkit.comp.IDeviceComponent;
import cc.iotkit.comps.config.CacheKey;
import cc.iotkit.comps.config.ComponentConfig;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.converter.*;
import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.data.IProductData;
import cc.iotkit.data.IProtocolComponentData;
import cc.iotkit.data.IProtocolConverterData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.protocol.ProtocolConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DeviceComponentManager {

    private final Map<String, IDeviceComponent> components = new HashMap<>();
    private final Map<String, Boolean> states = new HashMap<>();

    @Autowired
    private DeviceBehaviourService deviceBehaviourService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ComponentConfig componentConfig;
    @Autowired
    private IProtocolComponentData protocolComponentData;
    @Autowired
    @Qualifier("productDataCache")
    IProductData productData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private DeviceRouter deviceRouter;

    @Autowired
    private IProtocolConverterData protocolConverterData;

    private final IScriptConvertFactory scriptConverterFactory;

    public DeviceComponentManager(IScriptConvertFactory scriptConverterFactory) {
        this.scriptConverterFactory = scriptConverterFactory;
    }

    @PostConstruct
    public void init() {
        List<ProtocolComponent> componentList = protocolComponentData.findByStateAndType(
                ProtocolComponent.STATE_RUNNING, ProtocolComponent.TYPE_DEVICE);
        for (ProtocolComponent component : componentList) {
            try {
                register(component);
                start(component.getId());
            } catch (Throwable e) {
                log.error("init protocol components error", e);
            }
        }
    }

    public void register(ProtocolComponent component) {
        String id = component.getId();
        if (components.containsKey(id)) {
            return;
        }

        Path path = componentConfig.getComponentFilePath(id);
        File file = path.resolve(component.getJarFile()).toAbsolutePath().toFile();
        IDeviceComponent componentInstance;
        try {
            componentInstance = ComponentClassLoader.getComponent(component.getId(), file);
        } catch (Throwable e) {
            throw new BizException("get device component instance error", e);
        }
        componentInstance.create(new CompConfig(300, component.getConfig()));

        try {
            setScriptConvert(component, componentInstance);

            String componentScript = FileUtils.readFileToString(path.
                    resolve(ProtocolComponent.SCRIPT_FILE_NAME).toFile(), "UTF-8");
            componentInstance.setScript(componentScript);

            register(id, componentInstance);
        } catch (IOException e) {
            throw new BizException("get device component script error", e);
        }
    }

    private void setScriptConvert(ProtocolComponent component, IDeviceComponent componentInstance) throws IOException {
        ProtocolConverter protocolConvert = protocolConverterData.findById(component.getConverter());

        IConverter scriptConverter = scriptConverterFactory.getCovert(protocolConvert.getTyp());

        Path converterPath = componentConfig.getConverterFilePath(component.getConverter());
        String converterScript = FileUtils.readFileToString(converterPath.
                resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile(), "UTF-8");

//        scriptConverter.setScript(protocolConvert.getScript());
        scriptConverter.setScript(converterScript);
        scriptConverter.putScriptEnv("component", componentInstance);
        componentInstance.setConverter(scriptConverter);
    }

    public void register(String id, IDeviceComponent component) {
        components.put(id, component);
        states.put(id, false);
    }

    public void deRegister(String id) {
        IDeviceComponent component = components.remove(id);
        states.remove(id);
        if (component == null) {
            return;
        }
        component.stop();
        component.destroy();
    }

    public void start(String id) {
        IDeviceComponent component = components.get(id);
        if (component == null) {
            return;
        }
        DeviceMessageHandler messageHandler = new DeviceMessageHandler(
                this, component,
                component.getScript(), component.getConverter(),
                deviceBehaviourService, deviceRouter);
        messageHandler.putScriptEnv("apiTool", new ApiTool());
        messageHandler.putScriptEnv("deviceBehaviour", deviceBehaviourService);
        messageHandler.putScriptEnv("component", component);

        component.setHandler(messageHandler);
        component.start();
        states.put(id, true);
    }

    public void stop(String id) {
        IDeviceComponent component = components.get(id);
        if (component == null) {
            return;
        }
        component.stop();
        states.put(id, false);
    }

    public boolean isRunning(String id) {
        return states.containsKey(id) && states.get(id);
    }

    public void send(ThingService<?> service) {
        log.info("start exec device service:{}", JsonUtil.toJsonString(service));
        if (components.size() == 0) {
            throw new BizException("there is no components");
        }

        DeviceInfo deviceInfo = deviceInfoData.findByProductKeyAndDeviceName(service.getProductKey(), service.getDeviceName());
        Product product = productData.findById(service.getProductKey());
        String linkPk = service.getProductKey();
        String linkDn = service.getDeviceName();

        if (product.isTransparent()) {
            //如果是透传设备，取父级设备进行链路查找
            DeviceInfo parent = deviceInfoData.findByDeviceId(deviceInfo.getParentId());
            linkPk = parent.getProductKey();
            linkDn = parent.getDeviceName();
        }

        IComponent component = deviceRouter.getRouter(linkPk, linkDn);
        if (!(component instanceof IDeviceComponent)) {
            throw new BizException("send destination does not exist");
        }
        IDeviceComponent deviceComponent = (IDeviceComponent) component;

        //构建必要的设备信息
        Map<String, Object> tag = new HashMap<>();
        deviceInfo.getTag().forEach((k, v) -> tag.put(k, v.getValue()));
        Device device = new Device(deviceInfo.getDeviceId(),
                deviceInfo.getModel(),
                deviceInfo.getProperty(),
                tag,
                product.isTransparent());

        //对下发消息进行编码转换
        DeviceMessage message = deviceComponent.getConverter().encode(service, device);
        if (message == null) {
            throw new BizException("encode send message failed");
        }

        String sendMid = message.getMid();
        long timeout = deviceComponent.getConfig().getCmdTimeout();

        //保存设备端mid与平台mid对应关系
        saveMidMapping(message, timeout, service.getMid());
        //发送消息给设备
        message = deviceComponent.send(message);

        //mid发生改变
        if (!sendMid.equals(message.getMid())) {
            //重新保存消息id映射
            saveMidMapping(message, timeout, service.getMid());
        }

        //产生下发消息
        ThingModelMessage thingModelMessage = ThingModelMessage.builder()
                .mid(service.getMid())
                .productKey(service.getProductKey())
                .deviceName(service.getDeviceName())
                .identifier(service.getIdentifier())
                .type(service.getType())
                .data(service.getParams())
                .build();
        deviceBehaviourService.reportMessage(thingModelMessage);
    }

    /**
     * 保存设备端mid与平台mid对应关系
     */
    private void saveMidMapping(DeviceMessage message, long cmdTimeout, String serviceMid) {
        redisTemplate.opsForValue().set(
                CacheKey.getKeyCmdMid(message.getDeviceName(), message.getMid()),
                serviceMid, cmdTimeout, TimeUnit.SECONDS);
    }

    public String getPlatformMid(String deviceName, String mid) {
        return redisTemplate.opsForValue().get(CacheKey.getKeyCmdMid(deviceName, mid));
    }

}
