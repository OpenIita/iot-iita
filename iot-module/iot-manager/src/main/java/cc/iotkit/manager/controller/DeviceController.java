/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.data.manager.IDeviceConfigData;
import cc.iotkit.data.manager.IDeviceGroupData;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.model.InvokeResult;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceConfig;
import cc.iotkit.model.device.DeviceGroup;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.temporal.IDevicePropertyData;
import cc.iotkit.temporal.IThingModelMessageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = {"设备"})
@Slf4j
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private ProductController productController;
    @Lazy
    @Autowired
    private IThingModelMessageData thingModelMessageData;
    @Lazy
    @Autowired
    private IDevicePropertyData devicePropertyData;
    @Autowired
    private DeviceBehaviourService behaviourService;
    @Autowired
    DeferredDataConsumer deferredDataConsumer;
    @Autowired
    private IDeviceGroupData deviceGroupData;
    @Autowired
    private IDeviceConfigData deviceConfigData;

    @ApiOperation(value = "服务调用", notes = "服务调用", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataTypeClass = String.class),
            @ApiImplicitParam(name = "service", value = "服务", dataTypeClass = String.class),
            @ApiImplicitParam(name = "args", value = "参数", dataTypeClass = Map.class),
    })
    @PostMapping(Constants.API_DEVICE.INVOKE_SERVICE)
    public InvokeResult invokeService(@PathVariable("deviceId") String deviceId,
                                      @PathVariable("service") String service,
                                      @RequestBody Map<String, Object> args) {
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(service)) {
            throw new BizException(ErrCode.PARAMS_EXCEPTION);
        }
        return new InvokeResult(deviceService.invokeService(deviceId, service, args));
    }

    @ApiOperation(value = "属性获取", notes = "属性获取", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataTypeClass = String.class),
            @ApiImplicitParam(name = "propertyNames", value = "属性列表", dataTypeClass = ArrayList.class)
    })
    @PostMapping(Constants.API_DEVICE.INVOKE_SERVICE_PROPERTY_GET)
    public InvokeResult invokeServicePropertySet(@PathVariable("deviceId") String deviceId,
                                                 @RequestBody List<String> propertyNames) {
        if (StringUtils.isBlank(deviceId)) {
            throw new BizException(ErrCode.PARAMS_EXCEPTION);
        }
        return new InvokeResult(deviceService.getProperty(deviceId, propertyNames, true));
    }

    @ApiOperation(value = "属性设置", notes = "属性设置", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备ID", dataTypeClass = String.class),
            @ApiImplicitParam(name = "args", value = "参数", dataTypeClass = Map.class)
    })
    @PostMapping(Constants.API_DEVICE.SET_PROPERTIES)
    public InvokeResult setProperty(@PathVariable("deviceId") String deviceId,
                                    @RequestBody Map<String, Object> args) {
        return new InvokeResult(deviceService.setProperty(deviceId, args));
    }

    @ApiOperation(value = "设备列表", notes = "设备列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "长度", dataTypeClass = Integer.class, paramType = "path"),
            @ApiImplicitParam(name = "page", value = "页数", dataTypeClass = Integer.class, paramType = "path")
    })
    @PostMapping("/list/{size}/{page}")
    public Paging<DeviceInfo> getDevices(
            @PathVariable("size") int size,
            @PathVariable("page") int page,
            @RequestBody DeviceQuery query) {
        String uid = "";
        String subUid = "";
        if (!AuthUtil.isAdmin()) {
            //客户端用户使用绑定子用户查询
            if (AuthUtil.isClientUser()) {
                subUid = AuthUtil.getUserId();
            } else {
                uid = AuthUtil.getUserId();
            }
        }

        String pk = query.getProductKey();
        //关键字查询
        String keyword = query.getKeyword();
        String group = query.getGroup();
        String state = query.getState();

        return deviceInfoData.findByConditions(uid, subUid, pk, group,
                state, keyword, page, size);
    }

    @ApiOperation(value = "创建设备", notes = "创建设备", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productKey", value = "产品key", dataTypeClass = String.class, paramType = "form"),
            @ApiImplicitParam(name = "deviceName", value = "设备名称", dataTypeClass = String.class, paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父设备ID", dataTypeClass = String.class, paramType = "form")
    })
    @PostMapping("/create")
    public void createDevice(String productKey, String deviceName, String parentId) {
        Product product = productData.findById(productKey);
        if (product == null) {
            throw new BizException(ErrCode.PRODUCT_NOT_FOUND);
        }

        //生成设备密钥
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        int maxPos = chars.length();
        StringBuilder secret = new StringBuilder();
        for (var i = 0; i < 16; i++) {
            secret.append(chars.charAt((int) Math.floor(Math.random() * maxPos)));
        }

        DeviceInfo device = new DeviceInfo();
        device.setId(DeviceUtil.newDeviceId(deviceName));
        device.setUid(product.getUid());
        device.setDeviceId(device.getId());
        device.setProductKey(productKey);
        device.setDeviceName(deviceName);
        device.setSecret(secret.toString());
        device.setState(new DeviceInfo.State(false, null, null));
        device.setCreateAt(System.currentTimeMillis());
        if (StringUtils.isNotBlank(parentId)) {
            device.setParentId(parentId);
        }
        deviceInfoData.save(device);
    }

    @ApiOperation(value = "获取子设备", notes = "获取子设备", httpMethod = "GET")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", dataTypeClass = String.class, paramType = "form")
    @GetMapping("/{deviceId}/children")
    public List<DeviceInfo> getChildren(@PathVariable("deviceId") String deviceId) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        if (deviceInfo == null) {
            throw new BizException(ErrCode.DEVICE_NOT_FOUND);
        }

        dataOwnerService.checkOwner(deviceInfo);
        return deviceInfoData.findByParentId(deviceId);
    }

    @GetMapping("/parentDevices")
    public List<Map<String, Object>> getParentDevices() {
        String uid = "";
        if (!AuthUtil.isAdmin()) {
            uid = AuthUtil.getUserId();
        }
        return deviceInfoData.findByProductNodeType(uid);
    }

    @GetMapping(Constants.API_DEVICE.DETAIL)
    public DeviceInfo getDetail(@PathVariable("deviceId") String deviceId) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);
        deviceInfo.setProperty(deviceInfoData.getProperties(deviceId));
        return deviceInfo;
    }

    @GetMapping("/{pk}/{dn}")
    public DeviceInfo getByPkDn(@PathVariable("pk") String pk,
                                @PathVariable("dn") String dn) {
        return dataOwnerService.checkOwner(
                deviceInfoData.findByProductKeyAndDeviceName(pk, dn));
    }

    @PostMapping("/{deviceId}/delete")
    public void deleteDevice(@PathVariable("deviceId") String deviceId) {
        deviceId = getDetail(deviceId).getDeviceId();
        deviceInfoData.deleteById(deviceId);
    }

    @PostMapping("/{deviceId}/logs/{size}/{page}")
    public Paging<ThingModelMessage> logs(
            @PathVariable("deviceId") String deviceId,
            @PathVariable("size") int size,
            @PathVariable("page") int page,
            String type, String identifier) {
        return thingModelMessageData.findByTypeAndIdentifier(deviceId, type, identifier, page, size);
    }

    @GetMapping("/{deviceId}/property/{name}/{start}/{end}")
    public List<DeviceProperty> getPropertyHistory(
            @PathVariable("deviceId") String deviceId,
            @PathVariable("name") String name,
            @PathVariable("start") long start,
            @PathVariable("end") long end) {
        return devicePropertyData.findDevicePropertyHistory(deviceId, name, start, end);
    }

    @PostMapping("/{deviceId}/unbind")
    public void unbindDevice(@PathVariable("deviceId") String deviceId) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);
        deviceService.unbindDevice(deviceId);
    }

    @GetMapping("/{deviceId}/thingModel")
    public ThingModel getThingModel(@PathVariable("deviceId") String deviceId) {
        DeviceInfo deviceInfo = getDetail(deviceId);
        return productController.getThingModel(deviceInfo.getProductKey());
    }

    @PostMapping("/{deviceId}/tag/add")
    public void addTag(@PathVariable("deviceId") String deviceId,
                       DeviceInfo.Tag tag) {
        DeviceInfo device = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(device);
        deviceInfoData.updateTag(deviceId, tag);
    }

    @PostMapping("/{deviceId}/simulateSend")
    public void simulateSend(
            @PathVariable("deviceId") String deviceId,
            @RequestBody ThingModelMessage message) {
        DeviceInfo device = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(device);

        message.setMid(UniqueIdUtil.newRequestId());
        message.setOccurred(System.currentTimeMillis());
        message.setTime(System.currentTimeMillis());
        behaviourService.reportMessage(message);
    }

    /**
     * 消费设备信息消息（实时推送设备信息）
     */
    @GetMapping("/{deviceId}/consumer/{clientId}")
    public DeferredResult<ThingModelMessage> consumerDeviceInfo(
            @PathVariable("deviceId") String deviceId,
            @PathVariable("clientId") String clientId
    ) {
        String uid = AuthUtil.getUserId();
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);

        //按用户+客户端ID订阅
        return deferredDataConsumer.newConsumer(uid + clientId,
                Constants.HTTP_CONSUMER_DEVICE_INFO_TOPIC + deviceId);
    }

    /**
     * 获取分组列表
     */
    @PostMapping("/groups/{size}/{page}")
    public Paging<DeviceGroup> getDevices(
            @PathVariable("size") int size,
            @PathVariable("page") int page,
            String name
    ) {
        return deviceGroupData.findByNameLike(name, page, size);
    }

    /**
     * 添加设备分组
     */
    @PostMapping("/group/add")
    public void addGroup(DeviceGroup group) {
        group.setUid(AuthUtil.getUserId());
        if (deviceGroupData.findById(group.getId()) != null) {
            throw new BizException(ErrCode.GROUP_ALREADY);
        }
        deviceGroupData.save(group);
    }

    /**
     * 修改设备分组
     */
    @PostMapping("/group/save")
    public void saveGroup(DeviceGroup group) {
        DeviceGroup dbGroup = deviceGroupData.findById(group.getId());
        if (dbGroup == null) {
            throw new BizException(ErrCode.GROUP_NOT_FOUND);
        }
        dataOwnerService.checkOwner(dbGroup);
        ReflectUtil.copyNoNulls(group, dbGroup);

        deviceGroupData.save(dbGroup);
        //更新设备中的组信息
        deviceInfoData.updateGroup(dbGroup.getId(), new DeviceInfo.Group(dbGroup.getId(), dbGroup.getName()));
    }

    /**
     * 删除分组
     */
    @DeleteMapping("/group/delete/{id}")
    public void deleteGroup(@PathVariable("id") String id) {
        DeviceGroup group = deviceGroupData.findById(id);
        if (group == null) {
            throw new BizException(ErrCode.GROUP_NOT_FOUND);
        }
        dataOwnerService.checkOwner(group);
        //删除分组
        deviceGroupData.deleteById(id);

        //移除设备信息中的分组
        deviceInfoData.removeGroup(group.getId());
    }

    /**
     * 清空组下所有设备
     */
    @PostMapping("/group/clear/{id}")
    public void clearGroup(@PathVariable("id") String id) {
        DeviceGroup group = deviceGroupData.findById(id);
        if (group == null) {
            throw new BizException(ErrCode.GROUP_NOT_FOUND);
        }
        dataOwnerService.checkOwner(group);

        //设备数量清零
        group.setDeviceQty(0);
        deviceGroupData.save(group);

        //移除设备信息中的分组
        deviceInfoData.removeGroup(group.getId());
    }

    /**
     * 添加设备到组
     */
    @PostMapping("/group/addDevices/{group}")
    public void addToGroup(@PathVariable("group") String group, @RequestBody List<String> devices) {
        DeviceGroup deviceGroup = deviceGroupData.findById(group);
        if (deviceGroup == null) {
            throw new BizException(ErrCode.GROUP_NOT_FOUND);
        }
        dataOwnerService.checkOwner(deviceGroup);

        for (String device : devices) {
            DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(device);
            if (deviceInfo == null) {
                continue;
            }

            dataOwnerService.checkOwner(deviceInfo);
            //添加设备到组
            deviceInfoData.addToGroup(device, new DeviceInfo.Group(group, deviceGroup.getName()));
        }
        //统计组下设备数量
        long qty = deviceInfoData.countByGroupId(group);
        //更新组信息
        deviceGroup.setDeviceQty((int) qty);
        deviceGroupData.save(deviceGroup);
    }

    /**
     * 将设备从组中移除
     */
    @PostMapping("/group/removeDevices/{group}")
    public void removeDevices(@PathVariable("group") String group, @RequestBody List<String> devices) {
        DeviceGroup deviceGroup = deviceGroupData.findById(group);
        if (deviceGroup == null) {
            throw new BizException(ErrCode.GROUP_NOT_FOUND);
        }
        dataOwnerService.checkOwner(deviceGroup);

        for (String device : devices) {
            DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(device);
            if (deviceInfo == null) {
                continue;
            }

            dataOwnerService.checkOwner(deviceInfo);
            //删除设备所在组
            deviceInfoData.removeGroup(device, group);
        }
        //统计组下设备数量
        long qty = deviceInfoData.countByGroupId(group);
        //更新组信息
        deviceGroup.setDeviceQty((int) qty);
        deviceGroupData.save(deviceGroup);
    }

    /**
     * 保存设备配置
     */
    @PostMapping("/config/{deviceId}/save")
    public void saveConfig(@PathVariable("deviceId") String deviceId, String config) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);

        DeviceConfig deviceConfig = deviceConfigData.findByDeviceId(deviceId);
        if (deviceConfig == null) {
            deviceConfig = DeviceConfig.builder()
                    .deviceId(deviceId)
                    .deviceName(deviceInfo.getDeviceName())
                    .productKey(deviceInfo.getProductKey())
                    .config(config)
                    .createAt(System.currentTimeMillis())
                    .build();
        } else {
            deviceConfig.setConfig(config);
        }

        deviceConfigData.save(deviceConfig);
    }

    /**
     * 获取设备配置
     */
    @GetMapping("/config/{deviceId}/get")
    public DeviceConfig getConfig(@PathVariable("deviceId") String deviceId) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);
        return deviceConfigData.findByDeviceId(deviceId);
    }

    /**
     * 设备配置下发
     */
    @PostMapping("/config/{deviceId}/send")
    public InvokeResult sendConfig(@PathVariable("deviceId") String deviceId) {
        return new InvokeResult(deviceService.sendConfig(deviceId));
    }

}
