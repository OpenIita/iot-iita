/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.ruleengine.handler;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RuleDeviceConsumer implements ConsumerHandler<ThingModelMessage> {

    private final List<DeviceMessageHandler> handlers = new ArrayList<>();

    @SneakyThrows
    public RuleDeviceConsumer(MqConsumer<ThingModelMessage> consumer, List<DeviceMessageHandler> handlers) {
        this.handlers.addAll(handlers);
        consumer.consume(Constants.THING_MODEL_MESSAGE_TOPIC, this);
    }

    @SneakyThrows
    @Override
    public void handler(ThingModelMessage msg) {
        log.info("received thing model message:{}", JsonUtils.toJsonString(msg));
        try {
            for (DeviceMessageHandler handler : this.handlers) {
                handler.handle(msg);
            }
        } catch (Throwable e) {
            log.error("rule device message process error", e);
        }
    }

}
