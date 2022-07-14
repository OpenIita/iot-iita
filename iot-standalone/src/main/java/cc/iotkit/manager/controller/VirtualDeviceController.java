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

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.IVirtualDeviceData;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.temporal.IVirtualDeviceLogData;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.virtualdevice.VirtualManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        String uid = AuthUtil.getUserId();
        page = page - 1;
        if (AuthUtil.isAdmin()) {
            return virtualDeviceData.findAll(page, size);
        } else {
            return virtualDeviceData.findByUid(uid, page, size);
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
            throw new BizException("state is illegal");
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
        return virtualDeviceLogData.findByVirtualDeviceId(id, page - 1, size);
    }

    private VirtualDevice checkOwner(String id) {
        VirtualDevice oldData = virtualDeviceData.findById(id);
        if (oldData == null) {
            throw new BizException("record does not exist");
        }

        dataOwnerService.checkOwner(oldData);
        return oldData;
    }

}
