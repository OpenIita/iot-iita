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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.manager.dto.bo.device.*;
import cc.iotkit.manager.dto.bo.deviceconfig.DeviceConfigAddBo;
import cc.iotkit.manager.dto.bo.devicegroup.DeviceAddGroupBo;
import cc.iotkit.manager.dto.bo.devicegroup.DeviceGroupBo;
import cc.iotkit.manager.dto.bo.thingmodel.ThingModelMessageBo;
import cc.iotkit.manager.dto.vo.deviceconfig.DeviceConfigVo;
import cc.iotkit.manager.dto.vo.devicegroup.DeviceGroupVo;
import cc.iotkit.manager.dto.vo.deviceinfo.DeviceInfoVo;
import cc.iotkit.manager.dto.vo.thingmodel.ThingModelVo;
import cc.iotkit.manager.service.*;
import cc.iotkit.model.InvokeResult;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.device.DeviceConfig;
import cc.iotkit.model.device.DeviceGroup;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.ThingModelMessage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    IProductService productService;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private IDeviceService deviceServiceImpl;


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
    @PostMapping("/list")
    public Paging<DeviceInfo> getDevices(@Validated @RequestBody PageRequest<DeviceQueryBo> pageRequest) {

        return deviceServiceImpl.getDevices(pageRequest);
    }

    @ApiOperation(value = "创建设备")
    @PostMapping("/add")
    public boolean createDevice(@RequestBody @Validated Request<DeviceInfoBo> bo) {
        return deviceServiceImpl.addDevice(bo.getData());
    }

    @ApiOperation(value = "获取子设备", notes = "获取子设备", httpMethod = "GET")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", dataTypeClass = String.class, paramType = "form")
    @PostMapping("/children/list")
    public List<DeviceInfoVo> getChildren(@Validated @RequestBody PageRequest<String> request) {
        String deviceId = request.getData();

        return deviceServiceImpl.selectChildrenPageList(deviceId);
    }

    @ApiOperation("获取网关")
    @PostMapping("/parentDevices")
    public List<Map<String, Object>> getParentDevices() {
        return deviceServiceImpl.getParentDevices();
    }

    @ApiOperation("获取设备详情")
    @PostMapping(Constants.API_DEVICE.DETAIL)
    public DeviceInfo getDetail(@PathVariable("deviceId") Request<String> deviceId) {
        return deviceServiceImpl.getDetail(deviceId.getData());
    }

    @ApiOperation("获取设备详情")
    @PostMapping("/getByPkDn")
    public DeviceInfo getByPkDn(@Validated @RequestBody Request<DeviceQueryByPkDnBo> query) {
        return deviceServiceImpl.getByPkDn(query.getData().getPk(), query.getData().getDn());
    }

    @ApiOperation("删除设备")
    @PostMapping("/delete")
    public boolean deleteDevice(@Validated @RequestBody Request<String> request) {
        return deviceServiceImpl.deleteDevice(request.getData());
    }

    @ApiOperation("设备物模型日志")
    @PostMapping("/deviceLogs/list")
    public Paging<ThingModelMessage> logs(@Validated @RequestBody PageRequest<DeviceLogQueryBo> request) {
        return deviceServiceImpl.logs(request);
    }

    @ApiOperation("设备属性日志")
    @PostMapping("/deviceProperty/log/list")
    public List<DeviceProperty> getPropertyHistory(@Validated @RequestBody
                                                   Request<DevicePropertyLogQueryBo> query) {
        DevicePropertyLogQueryBo data = query.getData();
        String deviceId = data.getDeviceId();
        String name = data.getName();
        long start = data.getStart();
        long end = data.getEnd();
        return deviceServiceImpl.getPropertyHistory(deviceId, name, start, end);
    }

    @ApiOperation("设备解绑")
    @PostMapping("/unbind")
    public boolean unbindDevice(@Validated @RequestBody Request<String> request) {
        return deviceServiceImpl.unbindDevice(request.getData());
    }

    @ApiOperation("获取设备物模型")
    @PostMapping("/getThingModel")
    public ThingModelVo getThingModel(@Validated @RequestBody Request<String> request) {
        String deviceId = request.getData();
        DeviceInfo deviceInfo = deviceServiceImpl.getDetail(deviceId);
        return productService.getThingModelByProductKey(deviceInfo.getProductKey());
    }

    @PostMapping("/tag/add")
    public boolean addTag(@Validated @RequestBody Request<DeviceTagAddBo> bo) {
        return deviceServiceImpl.addTag(bo.getData());
    }

    @ApiOperation("模拟设备上报")
    @PostMapping("/simulateSend")
    public boolean simulateSend(
            @Validated @RequestBody Request<ThingModelMessageBo> bo) {
        ThingModelMessage message = bo.getData().to(ThingModelMessage.class);
        return deviceServiceImpl.simulateSend(message);
    }

    /**
     * 消费设备信息消息（实时推送设备信息）
     */
    @PostMapping("/consumer")
    public DeferredResult<ThingModelMessage> consumerDeviceInfo(
            @Validated @RequestBody Request<DeviceConsumerBo> bo
    ) {
        DeviceConsumerBo data = bo.getData();
        return deviceServiceImpl.addConsumer(data.getDeviceId(), data.getClientId());
    }

    /**
     * 获取分组列表
     */
    @PostMapping("/groups/list")
    public Paging<DeviceGroupVo> getDeviceGroups(
            @Validated @RequestBody PageRequest<DeviceGroupBo> pageRequest) {
        return deviceServiceImpl.selectGroupPageList(pageRequest);
    }

    /**
     * 添加设备分组
     */
    @ApiOperation(value = "添加设备分组")
    @PostMapping("/group/add")
    public boolean addGroup(@Validated @RequestBody Request<DeviceGroupBo> group) {
        return deviceServiceImpl.addGroup(group.getData().to(DeviceGroup.class));
    }

    /**
     * 修改设备分组
     */
    @ApiOperation(value = "修改设备分组")
    @PostMapping("/group/edit")
    public boolean editGroup(@RequestBody @Validated Request<DeviceGroupBo> bo) {
        return deviceServiceImpl.updateGroup(bo.getData());

    }

    /**
     * 删除分组
     */
    @ApiOperation(value = "删除分组")
    @DeleteMapping("/group/delete")
    public boolean deleteGroup(@Validated @RequestBody Request<String> request) {
        String id = request.getData();
        return deviceServiceImpl.deleteGroup(id);
    }

    /**
     * 清空组下所有设备
     */
    @ApiOperation(value = "清空组下所有设备")
    @PostMapping("/group/clear")
    public boolean clearGroup(@Validated @RequestBody Request<String> request) {
        String id = request.getData();
        return deviceServiceImpl.clearGroup(id);
    }

    /**
     * 添加设备到组
     */
    @ApiOperation(value = "添加设备到组")
    @PostMapping("/group/addDevices")
    public boolean addToGroup(@Validated @RequestBody Request<DeviceAddGroupBo> bo) {
        return deviceServiceImpl.addDevice2Group(bo.getData());
    }

    /**
     * 将设备从组中移除
     */
    @ApiOperation(value = "将设备从组中移除")
    @PostMapping("/group/removeDevices")
    public boolean removeDevices(@Validated @RequestBody Request<DeviceAddGroupBo> bo) {
        DeviceAddGroupBo data = bo.getData();
       return deviceServiceImpl.removeDevices(data.getGroup(), data.getDevices());
    }

    /**
     * 保存设备配置
     */
    @ApiOperation(value = "保存设备配置")
    @PostMapping("/config/save")
    public boolean saveConfig(@Validated @RequestBody Request<DeviceConfigAddBo> request) {
        DeviceConfig data = request.getData().to(DeviceConfig.class);
        return deviceServiceImpl.saveConfig(data);
    }

    /**
     * 获取设备配置
     */
    @ApiOperation(value = "获取设备配置")
    @PostMapping("/config/get")
    public DeviceConfigVo getConfig(@Validated @RequestBody Request<String> request) {
        String deviceId = request.getData();
        return deviceServiceImpl.getConfig(deviceId);
    }

    /**
     * 设备配置下发
     */
    @ApiOperation(value = "设备配置下发")
    @PostMapping("/config/send")
    public InvokeResult sendConfig(@Validated @RequestBody Request<String> bo) {
        String deviceId = bo.getData();
        return new InvokeResult(deviceService.sendConfig(deviceId));
    }

}
