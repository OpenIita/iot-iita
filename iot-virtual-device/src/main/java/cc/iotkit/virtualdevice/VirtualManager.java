/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.virtualdevice;

import cc.iotkit.common.thing.ThingService;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.dao.DeviceCache;
import cc.iotkit.dao.VirtualDeviceLogRepository;
import cc.iotkit.dao.VirtualDeviceRepository;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.virtualdevice.trigger.RandomScheduleBuilder;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngineManager;
import java.util.*;

@Slf4j
public class VirtualManager {
    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private final Map<String, Object> virtualScripts = new HashMap<>();
    private final Map<String, Set<String>> deviceIdToVirtualId = new HashMap<>();

    @Autowired
    private VirtualDeviceRepository virtualDeviceRepository;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private DeviceBehaviourService deviceBehaviourService;
    @Autowired
    private VirtualDeviceLogRepository virtualDeviceLogRepository;


    @PostConstruct
    public void init() {
        List<VirtualDevice> virtualDevices = getAllVirtualDevices();
        for (VirtualDevice virtualDevice : virtualDevices) {
            addTask(virtualDevice);
        }
    }

    /**
     * 判断设备是否应用了虚拟设备
     */
    public boolean isVirtual(String deviceId) {
        return deviceIdToVirtualId.containsKey(deviceId);
    }

    /**
     * 调用虚拟设备下发
     */
    public void send(ThingService<?> service) {
        DeviceInfo deviceInfo = deviceCache.getDeviceInfo(service.getProductKey(), service.getDeviceName());
        String deviceId = deviceInfo.getDeviceId();

        //根据设备Id取虚拟设备列表
        Set<String> virtualIds = deviceIdToVirtualId.get(deviceId);
        for (String virtualId : virtualIds) {
            Object scriptObj = virtualScripts.get(virtualId);
            Object result = invokeMethod(scriptObj, "receive", service);
            for (Object value : ((ScriptObjectMirror) result).values()) {
                processReport(value);
            }
            log.info("virtual device send result:{}", JsonUtil.toJsonString(result));
        }
    }


    /**
     * 添加虚拟设备
     */
    public void add(VirtualDevice virtualDevice) {
        addTask(virtualDevice);
    }

    /**
     * 删除虚拟设备
     */
    public void remove(VirtualDevice virtualDevice) {
        deleteTask(virtualDevice);
    }

    /**
     * 立即执行一次虚拟设备上报
     */
    public void run(VirtualDevice virtualDevice) {
        List<String> devices = virtualDevice.getDevices();
        VirtualDeviceLog virtualDeviceLog = VirtualDeviceLog.builder()
                .id(UUID.randomUUID().toString())
                .virtualDeviceId(virtualDevice.getId())
                .virtualDeviceName(virtualDevice.getName())
                .deviceTotal(devices.size())
                .result("success")
                .logAt(System.currentTimeMillis())
                .build();
        try {
            Object scriptObj = engine.eval(String.format("new (function () {\n%s})()", virtualDevice.getScript()));
            for (String deviceId : devices) {
                DeviceInfo device = deviceCache.get(deviceId);
                processReport(invokeMethod(scriptObj, "report", device));
            }
        } catch (Throwable e) {
            virtualDeviceLog.setResult(e.getMessage());
            log.error("run VirtualDevice error", e);
        }
        virtualDeviceLogRepository.save(virtualDeviceLog);
    }

    /**
     * 更新虚拟设备
     */
    public void update(VirtualDevice virtualDevice) {
        remove(virtualDevice);
        add(virtualDevice);
    }

    /**
     * 获取所有虚拟设备
     */
    private List<VirtualDevice> getAllVirtualDevices() {
        List<VirtualDevice> randomVirtualDevices = virtualDeviceRepository
                .findByTriggerAndState(VirtualDevice.TRIGGER_RANDOM, VirtualDevice.STATE_RUNNING);
        List<VirtualDevice> cronVirtualDevices = virtualDeviceRepository
                .findByTriggerAndState(VirtualDevice.TRIGGER_CRON, VirtualDevice.STATE_RUNNING);
        cronVirtualDevices.addAll(randomVirtualDevices);
        return cronVirtualDevices;
    }

    private void addTask(VirtualDevice virtualDevice) {
        try {
            String id = virtualDevice.getId();
            String name = virtualDevice.getName();
            String script = virtualDevice.getScript();
            log.info("adding virtual device job,id:{},name:{}", id, name);

            //添加新的脚本对象
            virtualScripts.put(id, engine.eval(String.format("new (function () {\n%s})()", script)));
            List<DeviceInfo> devices = new ArrayList<>();
            for (String deviceId : virtualDevice.getDevices()) {
                devices.add(deviceCache.get(deviceId));
                //更新deviceId的虚拟设备Id对应关系
                Set<String> virtualIds = deviceIdToVirtualId.getOrDefault(deviceId, new HashSet<>());
                virtualIds.add(id);
                deviceIdToVirtualId.put(deviceId, virtualIds);
            }

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("virtualManager", this);
            jobDataMap.put("virtualDevice", virtualDevice);
            jobDataMap.put("devices", devices);

            JobDetail jobDetail = JobBuilder.newJob(VirtualExecutor.class)
                    .withIdentity(id, name)
                    .usingJobData(jobDataMap)
                    .build();

            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("trigger_" + id, "triggerGroup_" + name)
                    .startNow()
                    .withSchedule(
                            getTriggerBuilder(virtualDevice)
                    ).build();

            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Throwable e) {
            log.error("create job failed", e);
        }
    }

    private ScheduleBuilder<?> getTriggerBuilder(VirtualDevice virtualDevice) {
        String type = virtualDevice.getTrigger();
        if ("random".equals(type)) {
            return new RandomScheduleBuilder(virtualDevice.getTriggerExpression());
        }
        if ("cron".equals(type)) {
            return CronScheduleBuilder.cronSchedule(virtualDevice.getTriggerExpression());
        }
        return null;
    }

    @SneakyThrows
    public void deleteTask(VirtualDevice virtualDevice) {
        String id = virtualDevice.getId();
        String name = virtualDevice.getName();

        //删除脚本对象
        virtualScripts.remove(id);

        //更新deviceId的虚拟设备Id对应关系
        for (String deviceId : deviceIdToVirtualId.keySet()) {
            Set<String> virtualIds = deviceIdToVirtualId.get(deviceId);
            virtualIds.remove(id);
        }

        //删除job
        TriggerKey triggerKey = new TriggerKey("trigger_" + id, "triggerGroup_" + name);
        if (!scheduler.checkExists(triggerKey)) {
            return;
        }
        scheduler.deleteJob(JobKey.jobKey(id, name));
    }

    /**
     * 处理js上报方法返回结果
     */
    public void processReport(Object sourceMsg) {
        try {
            ScriptObjectMirror result = (ScriptObjectMirror) sourceMsg;
            ThingModelMessage modelMessage = new ThingModelMessage();
            BeanUtils.populate(modelMessage, result);
            deviceBehaviourService.reportMessage(modelMessage);
        } catch (Throwable e) {
            log.error("process js data error", e);
        }
    }

    /**
     * 调用js方法
     */
    private Object invokeMethod(Object scriptObj, String name, Object... args) {
        try {
            if (((ScriptObjectMirror) scriptObj).get(name) != null) {
                return engine.invokeMethod(scriptObj, name, args);
            }
            return null;
        } catch (Throwable e) {
            log.error("invoke js method error", e);
        }
        return null;
    }

    /**
     * 调用脚本中上报方法
     */
    public void invokeReport(DeviceInfo device) {
        //设备上线
        deviceOnline(device);

        String deviceId = device.getDeviceId();
        Set<String> virtualIds = deviceIdToVirtualId.get(deviceId);
        if (virtualIds == null) {
            return;
        }

        for (String virtualId : virtualIds) {
            Object scriptObj = virtualScripts.get(virtualId);
            if (scriptObj == null) {
                continue;
            }
            processReport(invokeMethod(scriptObj, "report", device));
        }
    }

    /**
     * 设备上线
     */
    private void deviceOnline(DeviceInfo device) {
        DeviceInfo.State state = device.getState();
        if (state == null || !state.isOnline()) {
            //设备离线，产生上线消息
            deviceBehaviourService.deviceStateChange(device.getProductKey(), device.getDeviceName(), true);
        }
    }

    /**
     * 保存虚拟设备日志
     */
    public void saveLog(VirtualDeviceLog log) {
        virtualDeviceLogRepository.save(log);
    }

}
