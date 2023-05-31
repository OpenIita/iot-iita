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
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.manager.IVirtualDeviceData;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.model.product.Product;
import cc.iotkit.temporal.IVirtualDeviceLogData;
import cc.iotkit.virtualdevice.VirtualManager;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/list/{size}/{page}")
    public Paging<VirtualDevice> getDevices(
            PageRequest<VirtualDevice> pageRequest) {
        String uid = AuthUtil.getUserId();
        if (AuthUtil.isAdmin()) {
            return virtualDeviceData.findAll(pageRequest);
        } else {
            return virtualDeviceData.findByUid(uid, pageRequest.getPageNum(), pageRequest.getPageNum());
        }
    }

    /**
     * 添加虚拟设备
     */
    @PostMapping("/add")
    public void add(VirtualDevice virtualDevice) {
        virtualDevice.setId(null);
        virtualDevice.setUid(AuthUtil.getUserId());
        virtualDevice.setState(VirtualDevice.STATE_STOPPED);
        virtualDevice.setCreateAt(System.currentTimeMillis());
        virtualDeviceData.save(virtualDevice);
    }

    /**
     * 修改虚拟设备
     */
    @PostMapping("/modify")
    public void modify(VirtualDevice virtualDevice) {
        VirtualDevice oldData = checkOwner(virtualDevice.getId());
        ReflectUtil.copyNoNulls(virtualDevice, oldData,
                "name", "productKey", "type", "trigger", "triggerExpression");
        virtualDevice.setState(VirtualDevice.STATE_STOPPED);
        virtualDeviceData.save(virtualDevice);
    }

    /**
     * 获取虚拟设备详情
     */
    @GetMapping("/{id}/detail")
    public VirtualDevice detail(@PathVariable("id") String id) {
        return checkOwner(id);
    }

    /**
     * 设置虚拟设备状态
     */
    @PostMapping("/{id}/setState")
    public void setState(@PathVariable("id") String id, String state) {
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
    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable("id") String id) {
        checkOwner(id);
        virtualDeviceData.deleteById(id);
    }

    /**
     * 保存脚本
     */
    @PostMapping("/{id}/saveScript")
    public void saveScript(@PathVariable("id") String id, String script) {
        VirtualDevice old = checkOwner(id);
        old.setScript(script);
        virtualDeviceData.save(old);
    }

    /**
     * 保存关联设备
     */
    @PostMapping("/{id}/saveDevices")
    public void saveDevices(@PathVariable("id") String id, @RequestBody List<String> devices) {
        VirtualDevice old = checkOwner(id);
        old.setDevices(devices);
        virtualDeviceData.save(old);
    }

    /**
     * 手动执行虚拟设备
     */
    @PostMapping("/{id}/run")
    public void run(@PathVariable("id") String id) {
        VirtualDevice virtualDevice = checkOwner(id);
        virtualManager.run(virtualDevice);
    }

    /**
     * 取虚拟设备执行日志
     */
    @PostMapping("/{id}/logs/{size}/{page}")
    public Paging<VirtualDeviceLog> getLogs(
            @PathVariable("id") String id,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        return virtualDeviceLogData.findByVirtualDeviceId(id, page, size);
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
