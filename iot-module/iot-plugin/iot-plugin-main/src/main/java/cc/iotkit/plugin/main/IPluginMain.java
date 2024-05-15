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

package cc.iotkit.plugin.main;

import cc.iotkit.common.thing.ThingService;
import cc.iotkit.plugin.core.IPluginConfig;
import cc.iotkit.plugin.core.IPluginScript;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 主程序接口
 *
 * @author sjg
 */
@RestController
@RequestMapping("/plugin")
public interface IPluginMain extends IPluginScript, IPluginConfig {

    /**
     * 主程序id
     */
    String MAIN_ID = UUID.randomUUID().toString();

    /**
     * 调用设备服务
     *
     * @param service 设备服务
     */
    @PostMapping("/invoke")
    void invoke(@RequestBody ThingService<?> service);

}
