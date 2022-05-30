package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.dao.VirtualDeviceLogRepository;
import cc.iotkit.dao.VirtualDeviceRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.model.rule.TaskLog;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.virtualdevice.VirtualManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/virtual_device")
public class VirtualDeviceController {

    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private VirtualDeviceRepository virtualDeviceRepository;
    @Autowired
    private VirtualManager virtualManager;
    @Autowired
    private VirtualDeviceLogRepository virtualDeviceLogRepository;

    @PostMapping("/list/{size}/{page}")
    public Paging<VirtualDevice> getDevices(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        String uid = AuthUtil.getUserId();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createAt")));
        Page<VirtualDevice> virtualDevices;
        if (AuthUtil.isAdmin()) {
            virtualDevices = virtualDeviceRepository.findAll(pageable);
        } else {
            virtualDevices = virtualDeviceRepository.findByUid(uid, pageable);
        }
        return new Paging<>(virtualDevices.getTotalElements(), virtualDevices.getContent());
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
        virtualDeviceRepository.save(virtualDevice);
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
        virtualDeviceRepository.save(virtualDevice);
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
        virtualDeviceRepository.save(oldData);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable("id") String id) {
        checkOwner(id);
        virtualDeviceRepository.deleteById(id);
    }

    /**
     * 保存脚本
     */
    @PostMapping("/{id}/saveScript")
    public void saveScript(@PathVariable("id") String id, String script) {
        VirtualDevice old = checkOwner(id);
        old.setScript(script);
        virtualDeviceRepository.save(old);
    }

    /**
     * 保存关联设备
     */
    @PostMapping("/{id}/saveDevices")
    public void saveDevices(@PathVariable("id") String id, @RequestBody List<String> devices) {
        VirtualDevice old = checkOwner(id);
        old.setDevices(devices);
        virtualDeviceRepository.save(old);
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
        Page<VirtualDeviceLog> logs = virtualDeviceLogRepository.findByVirtualDeviceId(id,
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("logAt"))));
        return new Paging<>(logs.getTotalElements(), logs.getContent());
    }

    private VirtualDevice checkOwner(String id) {
        Optional<VirtualDevice> old = virtualDeviceRepository.findById(id);
        if (old.isEmpty()) {
            throw new BizException("record does not exist");
        }
        VirtualDevice oldData = old.get();

        dataOwnerService.checkOwner(oldData);
        return oldData;
    }

}
