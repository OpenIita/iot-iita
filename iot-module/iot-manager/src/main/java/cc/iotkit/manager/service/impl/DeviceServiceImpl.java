package cc.iotkit.manager.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.common.utils.DeviceUtil;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.data.manager.IDeviceConfigData;
import cc.iotkit.data.manager.IDeviceGroupData;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.manager.dto.bo.device.DeviceInfoBo;
import cc.iotkit.manager.dto.bo.device.DeviceLogQueryBo;
import cc.iotkit.manager.dto.bo.device.DeviceQueryBo;
import cc.iotkit.manager.dto.bo.device.DeviceTagAddBo;
import cc.iotkit.manager.dto.bo.devicegroup.DeviceAddGroupBo;
import cc.iotkit.manager.dto.bo.devicegroup.DeviceGroupBo;
import cc.iotkit.manager.dto.vo.deviceconfig.DeviceConfigVo;
import cc.iotkit.manager.dto.vo.devicegroup.DeviceGroupVo;
import cc.iotkit.manager.dto.vo.deviceinfo.DeviceInfoVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.DeferredDataConsumer;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.manager.service.IDeviceService;
import cc.iotkit.model.device.DeviceConfig;
import cc.iotkit.model.device.DeviceGroup;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.Product;
import cc.iotkit.temporal.IDevicePropertyData;
import cc.iotkit.temporal.IThingModelMessageData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;

/**
 * @Author: jay
 * @Date: 2023/5/31 11:06
 * @Version: V1.0
 * @Description: 设备服务实现
 */

@Service
public class DeviceServiceImpl implements IDeviceService {

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

    @Override
    public Paging<DeviceInfo> getDevices(PageRequest<DeviceQueryBo> pageRequest) {
        DeviceQueryBo query = pageRequest.getData();

        String uid = "";
        String subUid = "";
//        if (!AuthUtil.isAdmin()) {
//            //客户端用户使用绑定子用户查询
//            if (AuthUtil.isClientUser()) {
//                subUid = AuthUtil.getUserId();
//            } else {
//                uid = AuthUtil.getUserId();
//            }
//        }

        String pk = query.getProductKey();
        //关键字查询
        String keyword = query.getKeyword();
        String group = query.getGroup();
        String state = query.getState();

        return deviceInfoData.findByConditions(uid, subUid, pk, group,
                state, keyword, pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    @Override
    public boolean addDevice(DeviceInfoBo deviceInfo) {

        String productKey = deviceInfo.getProductKey();
        String deviceName = deviceInfo.getDeviceName();
        String parentId = deviceInfo.getParentId();

        Product product = productData.findByProductKey(productKey);
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
        return true;
    }

    @Override
    public List<DeviceInfoVo> selectChildrenPageList(String deviceId) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        if (deviceInfo == null) {
            throw new BizException(ErrCode.DEVICE_NOT_FOUND);
        }

        dataOwnerService.checkOwner(deviceInfo);
        return MapstructUtils.convert(deviceInfoData.findByParentId(deviceId), DeviceInfoVo.class);
    }

    @Override
    public List<Map<String, Object>> getParentDevices() {
        String uid = "";
        if (!AuthUtil.isAdmin()) {
            uid = AuthUtil.getUserId();
        }
        return deviceInfoData.findByProductNodeType(uid);
    }

    @Override
    public DeviceInfo getDetail(String deviceId) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
//        dataOwnerService.checkOwner(deviceInfo);
        deviceInfo.setProperty(deviceInfoData.getProperties(deviceId));
        return deviceInfo;
    }

    @Override
    public DeviceInfo getByPkDn(String pk, String dn) {
        return dataOwnerService.checkOwner(
                deviceInfoData.findByProductKeyAndDeviceName(pk, dn));
    }

    @Override
    public boolean deleteDevice(String deviceId) {

        deviceId = getDetail(deviceId).getDeviceId();
        deviceInfoData.deleteById(deviceId);
        return true;
    }

    @Override
    public Paging<ThingModelMessage> logs(PageRequest<DeviceLogQueryBo> request) {
        DeviceLogQueryBo data = request.getData();
        return thingModelMessageData.findByTypeAndIdentifier(data.getDeviceId(), data.getType(), data.getIdentifier(), request.getPageNum(), request.getPageSize());

    }

    @Override
    public List<DeviceProperty> getPropertyHistory(String deviceId, String name, long start, long end) {
        return devicePropertyData.findDevicePropertyHistory(deviceId, name, start, end);
    }

    @Override
    public boolean unbindDevice(String deviceId) {

        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);
        deviceService.unbindDevice(deviceId);
        return false;
    }

    @Override
    public boolean addTag(DeviceTagAddBo bo) {
        String deviceId = bo.getDeviceId();
        DeviceInfo device = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(device);
        deviceInfoData.updateTag(deviceId, bo.to(DeviceInfo.Tag.class));
        return false;
    }

    @Override
    public boolean simulateSend(ThingModelMessage message) {
        DeviceInfo device = deviceInfoData.findByDeviceId(message.getDeviceId());
        dataOwnerService.checkOwner(device);

        message.setMid(UniqueIdUtil.newRequestId());
        message.setOccurred(System.currentTimeMillis());
        message.setTime(System.currentTimeMillis());
        behaviourService.reportMessage(message);
        return false;
    }

    @Override
    public DeferredResult addConsumer(String deviceId, String clientId) {

        DeferredResult<ThingModelMessage> result = new DeferredResult<>(0L);
        String uid = AuthUtil.getUserId();
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);

        //按用户+客户端ID订阅
        return deferredDataConsumer.newConsumer(uid + clientId,
                Constants.HTTP_CONSUMER_DEVICE_INFO_TOPIC + deviceId);
    }

    @Override
    public Paging<DeviceGroupVo> selectGroupPageList(PageRequest<DeviceGroupBo> pageRequest) {
        DeviceGroupBo data = pageRequest.getData();
        return deviceGroupData.findByNameLike(data.getName(), pageRequest.getPageNum(), pageRequest.getPageSize()).to(DeviceGroupVo.class);
    }

    @Override
    public boolean addGroup(DeviceGroup group) {
        group.setUid(AuthUtil.getUserId());
        if (deviceGroupData.findById(group.getId()) != null) {
            throw new BizException(ErrCode.GROUP_ALREADY);
        }
        deviceGroupData.save(group);
        return true;
    }

    @Override
    public boolean updateGroup(DeviceGroupBo bo) {
        DeviceGroup group = bo.to(DeviceGroup.class);
        DeviceGroup dbGroup = deviceGroupData.findById(group.getId());
        if (dbGroup == null) {
            throw new BizException(ErrCode.GROUP_NOT_FOUND);
        }
        dataOwnerService.checkOwner(dbGroup);
        ReflectUtil.copyNoNulls(group, dbGroup);

        deviceGroupData.save(dbGroup);
        //更新设备中的组信息
        deviceInfoData.updateGroup(dbGroup.getId(), new DeviceInfo.Group(dbGroup.getId(), dbGroup.getName()));
        return true;
    }

    @Override
    public boolean deleteGroup(String id) {
        DeviceGroup group = deviceGroupData.findById(id);
        if (group == null) {
            throw new BizException(ErrCode.GROUP_NOT_FOUND);
        }
        dataOwnerService.checkOwner(group);
        //删除分组
        deviceGroupData.deleteById(id);

        //移除设备信息中的分组
        deviceInfoData.removeGroup(group.getId());
        return true;
    }

    @Override
    public boolean clearGroup(String id) {
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
        return true;
    }

    @Override
    public boolean addDevice2Group(DeviceAddGroupBo data) {

        String group = data.getGroup();
        List<String> devices = data.getDevices();
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
        return true;
    }

    @Override
    public boolean removeDevices(String group, List<String> devices) {

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
        return true;
    }

    @Override
    public boolean saveConfig(DeviceConfig data) {
        String deviceId = data.getDeviceId();
        String config = data.getConfig();
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
        return false;
    }

    @Override
    public DeviceConfigVo getConfig(String deviceId) {
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);
        return MapstructUtils.convert(deviceConfigData.findByDeviceId(deviceId), DeviceConfigVo.class);

    }


}
