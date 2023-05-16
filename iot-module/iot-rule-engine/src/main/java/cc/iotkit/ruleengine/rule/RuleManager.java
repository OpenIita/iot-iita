/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.ruleengine.rule;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IRuleInfoData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.RuleAction;
import cc.iotkit.model.rule.RuleInfo;
import cc.iotkit.ruleengine.action.*;
import cc.iotkit.ruleengine.action.device.DeviceAction;
import cc.iotkit.ruleengine.action.device.DeviceActionService;
import cc.iotkit.ruleengine.action.http.HttpAction;
import cc.iotkit.ruleengine.action.http.HttpService;
import cc.iotkit.ruleengine.action.kafka.KafkaAction;
import cc.iotkit.ruleengine.action.kafka.KafkaService;
import cc.iotkit.ruleengine.action.mqtt.MqttAction;
import cc.iotkit.ruleengine.action.mqtt.MqttService;
import cc.iotkit.ruleengine.action.tcp.TcpAction;
import cc.iotkit.ruleengine.action.tcp.TcpService;
import cc.iotkit.ruleengine.config.RuleConfiguration;
import cc.iotkit.ruleengine.filter.DeviceFilter;
import cc.iotkit.ruleengine.filter.Filter;
import cc.iotkit.ruleengine.link.LinkFactory;
import cc.iotkit.ruleengine.listener.DeviceListener;
import cc.iotkit.ruleengine.listener.Listener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RuleManager {

    @Autowired
    private RuleConfiguration ruleConfiguration;

    @Autowired
    private RuleMessageHandler ruleMessageHandler;

    @Autowired
    private IRuleInfoData ruleInfoData;

    @Autowired
    @Qualifier("deviceInfoPropertyDataCache")
    private IDeviceInfoData deviceInfoData;

    @Autowired
    private DeviceActionService deviceActionService;

    public RuleManager() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(this::initRules, 1, TimeUnit.SECONDS);
    }

    @SneakyThrows
    public void initRules() {
        int idx = 1;
        while (true) {
            Paging<RuleInfo> rules = ruleInfoData.findAll(idx, 1000);
            // 如果记录为空，直接跳出循环
            if (rules.getData() == null || rules.getData().isEmpty()) {
                break;
            }
            rules.getData().forEach(rule -> {
                try {
                    //不添加停止的规则
                    if (RuleInfo.STATE_STOPPED.equals(rule.getState())) {
                        return;
                    }
                    log.info("got rule {} to init", rule.getId());
                    add(rule);
                } catch (Throwable e) {
                    log.error("add rule error", e);
                }
            });
            idx++;
        }
    }

    public void add(RuleInfo ruleInfo) {
        Rule rule = parseRule(ruleInfo);
        ruleMessageHandler.putRule(rule);
    }

    public void remove(String ruleId) {
        ruleMessageHandler.removeRule(ruleId);
        // 移出link连接
        LinkFactory.ruleClose(ruleId);
    }

    public void pause(String ruleId) {
        remove(ruleId);
    }

    public void resume(RuleInfo ruleInfo) {
        add(ruleInfo);
    }

    private Rule parseRule(RuleInfo ruleInfo) {
        List<Listener<?>> listeners = new ArrayList<>();
        for (RuleInfo.Listener listener : ruleInfo.getListeners()) {
            listeners.add(parseListener(listener.getType(), listener.getConfig()));
        }
        List<Filter<?>> filters = new ArrayList<>();
        for (RuleInfo.Filter filter : ruleInfo.getFilters()) {
            filters.add(parseFilter(filter.getType(), filter.getConfig()));
        }
        List<Action<?>> actions = new ArrayList<>();
        for (RuleAction action : ruleInfo.getActions()) {
            actions.add(parseAction(ruleInfo.getId(), action.getType(), action.getConfig()));
        }

        return new Rule(ruleInfo.getId(), ruleInfo.getName(), listeners, filters, actions);
    }

    private Listener<?> parseListener(String type, String config) {
        if (DeviceListener.TYPE.equals(type)) {
            return parse(config, DeviceListener.class);
        }
        return null;
    }

    private Filter<?> parseFilter(String type, String config) {
        if (DeviceFilter.TYPE.equals(type)) {
            DeviceFilter filter = parse(config, DeviceFilter.class);
            filter.setDeviceInfoData(deviceInfoData);
            return filter;
        }
        return null;
    }

    private Action<?> parseAction(String ruleId, String type, String config) {
        if (DeviceAction.TYPE.equals(type)) {
            DeviceAction action = parse(config, DeviceAction.class);
            action.setDeviceActionService(deviceActionService);
            return action;
        } else if (HttpAction.TYPE.equals(type)) {
            HttpAction httpAction = parse(config, HttpAction.class);
            for (HttpService service : httpAction.getServices()) {
                service.setDeviceInfoData(deviceInfoData);
            }
            return httpAction;
        } else if (MqttAction.TYPE.equals(type)) {
            MqttAction mqttAction = parse(config, MqttAction.class);
            for (MqttService service : mqttAction.getServices()) {
                service.setDeviceInfoData(deviceInfoData);
                service.initLink(ruleId);
            }
            return mqttAction;
        } else if (KafkaAction.TYPE.equals(type)) {
            KafkaAction kafkaAction = parse(config, KafkaAction.class);
            for (KafkaService service : kafkaAction.getServices()) {
                service.setDeviceInfoData(deviceInfoData);
                service.initLink(ruleId);
            }
            return kafkaAction;
        } else if (TcpAction.TYPE.equals(type)) {
            TcpAction tcpAction = parse(config, TcpAction.class);
            for (TcpService service : tcpAction.getServices()) {
                service.setDeviceInfoData(deviceInfoData);
                service.initLink(ruleId);
            }
            return tcpAction;
        }
        return null;
    }

    private <T> T parse(String config, Class<T> cls) {
        return JsonUtil.parse(config, cls);
    }

}
