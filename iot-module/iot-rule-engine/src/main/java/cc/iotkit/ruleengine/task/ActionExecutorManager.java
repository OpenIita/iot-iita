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
package cc.iotkit.ruleengine.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ActionExecutorManager implements ApplicationContextAware {

    private static Map<String, ActionExecutor> EXECUTORS = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ActionExecutor> beans = applicationContext.getBeansOfType(ActionExecutor.class);
        beans.forEach((name, bean) -> EXECUTORS.put(bean.getName(), bean));
    }

    public ActionExecutor getExecutor(String name) {
        return EXECUTORS.get(name);
    }

    public void execute(String name, String data) {
        ActionExecutor<?> executor = getExecutor(name);
        if (executor == null) {
            log.error("action executor not found by name:{}", name);
            return;
        }

        executor.execute(data);
    }
}
