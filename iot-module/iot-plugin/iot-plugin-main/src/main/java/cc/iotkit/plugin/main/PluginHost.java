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

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.data.manager.IPluginInstanceData;
import cc.iotkit.model.plugin.PluginInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 插件接口调用入口
 *
 * @author sjg
 */
@Slf4j
@Service
public class PluginHost {

    @Autowired
    private IPluginInstanceData pluginInstanceData;

    @Autowired
    private DeviceRouter deviceRouter;

    @Autowired
    private IPluginMain pluginMain;

    public void invoke(ThingService<?> service) {
        log.info("start exec device service:{}", service);
        //根据设备获取插件路由
        PluginRouter router = deviceRouter.getRouter(service.getDeviceName());
        if (router == null) {
            throw new BizException(ErrCode.PLUGIN_ROUTER_NOT_FOUND);
        }

        //根据插件路由找到要调用的主程序
        PluginInstance instance = pluginInstanceData.findInstance(router.getMainId(), router.getPluginId());
        if (instance == null) {
            throw new BizException(ErrCode.PLUGIN_INSTANCE_NOT_FOUND);
        }

        //调用插件主程序接口
        pluginMain.invoke(service);
    }
}
