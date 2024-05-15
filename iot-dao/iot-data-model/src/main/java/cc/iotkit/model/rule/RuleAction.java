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
package cc.iotkit.model.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则引擎输出动作
 *
 * @author sjg
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleAction {
    /**
     * 设备控制
     */
    public static final String TYPE_DEVICE = "device";

    /**
     * 告警消息
     */
    public static final String TYPE_ALARM = "alarm";

    /**
     * 场景控制
     */
    public static final String TYPE_SCENE = "scene";

    /**
     * http推送
     */
    public static final String TYPE_HTTP = "http";

    /**
     * mqtt推送
     */
    public static final String TYPE_MQTT = "mqtt";

    /**
     * kafka推送
     */
    public static final String TYPE_KAFKA = "kafka";

    /**
     * tcp推送
     */
    public static final String TYPE_TCP = "tcp";

    /**
     * 动作类型
     */
    protected String type;

    /**
     * 动作配置
     */
    protected String config;

}
