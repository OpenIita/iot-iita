/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package cc.iotkit.ruleengine.handler;


import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.ThreadUtil;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Slf4j
public class RuleDeviceConsumer implements ConsumerHandler<ThingModelMessage>, ApplicationContextAware {

    private final List<DeviceMessageHandler> handlers = new ArrayList<>();
    private ScheduledThreadPoolExecutor messageHandlerPool;

    @SneakyThrows
    public RuleDeviceConsumer(MqConsumer<ThingModelMessage> consumer) {
        consumer.consume(Constants.THING_MODEL_MESSAGE_TOPIC, this);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, DeviceMessageHandler> handlerMap = applicationContext.getBeansOfType(DeviceMessageHandler.class);
        messageHandlerPool = ThreadUtil.newScheduled(handlerMap.size() * 2, "messageHandler");
        this.handlers.addAll(handlerMap.values());
    }

    @SneakyThrows
    @Override
    public void handler(ThingModelMessage msg) {
        log.info("received thing model message:{}", msg);
        try {
            for (DeviceMessageHandler handler : this.handlers) {
                messageHandlerPool.submit(() -> {
                    try {
                        if (!(msg.getData() instanceof Map)) {
                            msg.setData(new HashMap<>());
                        }
                        handler.handle(msg);
                    } catch (Throwable e) {
                        log.error("handler message error", e);
                    }
                });
            }
        } catch (Throwable e) {
            log.error("rule device message process error", e);
        }
    }

}
