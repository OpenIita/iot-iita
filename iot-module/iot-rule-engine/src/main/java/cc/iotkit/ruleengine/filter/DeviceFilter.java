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
package cc.iotkit.ruleengine.filter;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.data.manager.IDeviceInfoData;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class DeviceFilter implements Filter<DeviceCondition> {

    public static String TYPE = "device";

    private String type;

    private String pk;

    private String dn;

    private List<DeviceCondition> conditions;

    private IDeviceInfoData deviceInfoData;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean execute(ThingModelMessage msg) {
        for (DeviceCondition condition : getConditions()) {
            DeviceCondition con = condition.clone();
            //未指定device，使用消息中的deviceId
            if (StringUtils.isBlank(con.getDevice())) {
                con.setDevice(msg.getDeviceId());
            }

            con.setDeviceInfoData(deviceInfoData);
            if (!con.matches()) {
                return false;
            }
        }
        return true;
    }

}
