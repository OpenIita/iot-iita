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
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.manager.IVirtualDeviceData;
import cc.iotkit.manager.dto.bo.ChangeStateBo;
import cc.iotkit.manager.dto.bo.device.DeviceLogQueryBo;
import cc.iotkit.manager.dto.bo.device.DeviceSaveScriptBo;
import cc.iotkit.manager.dto.bo.virtualdevice.VirtualSaveDevicesBo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.temporal.IVirtualDeviceLogData;
import cc.iotkit.virtualdevice.VirtualManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"虚拟设备"})
@Slf4j
@RestController
@RequestMapping("/virtual_device")
public class VirtualDeviceController {

    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private IVirtualDeviceData virtualDeviceData;
    @Autowired
    private VirtualManager virtualManager;
    @Autowired
    private IVirtualDeviceLogData virtualDeviceLogData;

    @ApiOperation("获取虚拟设备列表")
    @PostMapping("/list")
    public Paging<VirtualDevice> getDevices(PageRequest<VirtualDevice> pageRequest) {
        return virtualDeviceData.findAll(pageRequest);
    }

    /**
     * 添加虚拟设备
     */
    @ApiOperation("添加虚拟设备")
    @PostMapping("/add")
    public void add(@Validated @RequestBody VirtualDevice virtualDevice) {
        virtualDeviceData.save(virtualDevice);
    }

    /**
     * 修改虚拟设备
     */
    @ApiOperation("修改虚拟设备")
    @PostMapping("/modify")
    public void modify(@Validated @RequestBody Request<VirtualDevice> bo) {
        VirtualDevice virtualDevice = bo.getData();
        VirtualDevice oldData = checkOwner(virtualDevice.getId());
        ReflectUtil.copyNoNulls(virtualDevice, oldData,
                "name", "productKey", "type", "trigger", "triggerExpression");
        virtualDevice.setState(VirtualDevice.STATE_STOPPED);
        virtualDeviceData.save(virtualDevice);
    }

    /**
     * 获取虚拟设备详情
     */
    @ApiOperation("获取虚拟设备详情")
    @PostMapping("/getDetail")
    public VirtualDevice detail(@Validated @RequestBody Request<String> bo) {
        return checkOwner(bo.getData());
    }

    /**
     * 设置虚拟设备状态
     */
    @ApiOperation("设置虚拟设备状态")
    @PostMapping("/setState")
    public void setState(@Validated @RequestBody Request<ChangeStateBo> bo) {
        ChangeStateBo data = bo.getData();
        String id = data.getId();
        String state = data.getState();
        VirtualDevice oldData = checkOwner(id);
        if (!VirtualDevice.STATE_RUNNING.equals(state)
                && !VirtualDevice.STATE_STOPPED.equals(state)) {
            throw new BizException(ErrCode.STATE_ERROR);
        }
        oldData.setState(state);
        if (VirtualDevice.STATE_RUNNING.equals(state)) {
            virtualManager.add(oldData);
        } else {
            virtualManager.remove(oldData);
        }
        virtualDeviceData.save(oldData);
    }

    /**
     * 删除
     */
    @ApiOperation("删除虚拟设备")
    @PostMapping("/delete")
    public void delete(@Validated @RequestBody Request<String> bo) {
        String id = bo.getData();
        checkOwner(id);
        virtualDeviceData.deleteById(id);
    }

    /**
     * 保存脚本
     */
    @ApiOperation("保存脚本")
    @PostMapping("/saveScript")
    public void saveScript(@Validated @RequestBody Request<DeviceSaveScriptBo> bo) {
        DeviceSaveScriptBo data = bo.getData();
        String id = data.getId();
        String script = data.getScript();
        VirtualDevice old = checkOwner(id);
        old.setScript(script);
        virtualDeviceData.save(old);
    }

    /**
     * 保存关联设备
     */
    @ApiOperation("保存关联设备")
    @PostMapping("/saveDevices")
    public void saveDevices(@Validated @RequestBody Request<VirtualSaveDevicesBo> bo) {
        VirtualSaveDevicesBo data = bo.getData();
        List<String> devices = data.getDevices();
        String id = data.getId();
        VirtualDevice old = checkOwner(id);
        old.setDevices(devices);
        virtualDeviceData.save(old);
    }

    /**
     * 手动执行虚拟设备
     */
    @ApiOperation("手动执行虚拟设备")
    @PostMapping("/run")
    public void run(@Validated @RequestBody Request<String> bo) {
        String id = bo.getData();
        VirtualDevice virtualDevice = checkOwner(id);
        virtualManager.run(virtualDevice);
    }

    /**
     * 取虚拟设备执行日志
     */
    @ApiOperation("取虚拟设备执行日志")
    @PostMapping("/logs/list")
    public Paging<VirtualDeviceLog> getLogs(
            @Validated @RequestBody PageRequest<DeviceLogQueryBo> bo) {
        DeviceLogQueryBo data = bo.getData();

        return virtualDeviceLogData.findByVirtualDeviceId(data.getDeviceId(), bo.getPageNum(), bo.getPageSize());
    }

    private VirtualDevice checkOwner(String id) {
        VirtualDevice oldData = virtualDeviceData.findById(id);
        if (oldData == null) {
            throw new BizException(ErrCode.RECORD_NOT_FOUND);
        }

        dataOwnerService.checkOwner(oldData);
        return oldData;
    }

}
