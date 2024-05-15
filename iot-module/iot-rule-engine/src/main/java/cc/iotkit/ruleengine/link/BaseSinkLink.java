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

package cc.iotkit.ruleengine.link;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author huangwenl
 * @date 2022-11-10
 */
public interface BaseSinkLink {

    /**
     * 建立连接
     * @param config  连接配置信息
     */
    boolean open(Map<String, Object> config);

    /**
     * 发送消息
     * @param msg 消息内容
     * @param consumer  发送回调
     */
    void send(Map<String, Object> msg, Consumer<String> consumer);

    /**
     * 关闭连接
     */
    void close();

    /**
     * 连接监听
     * @param closeHandler
     */
    void closeHandler(Consumer<Void> closeHandler);
}
