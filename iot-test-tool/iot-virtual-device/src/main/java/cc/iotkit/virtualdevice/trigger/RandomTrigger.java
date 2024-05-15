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
package cc.iotkit.virtualdevice.trigger;

import org.apache.commons.lang3.RandomUtils;
import org.quartz.Calendar;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

public class RandomTrigger extends SimpleTriggerImpl {

    private String unit;
    private Date nextFireTime;

    public RandomTrigger(String unit) {
        this.unit = unit;
    }

    @Override
    public void triggered(Calendar calendar) {
        super.triggered(calendar);
        nextFireTime = randomTime();
    }

    @Override
    public Date getNextFireTime() {
        if (nextFireTime == null) {
            nextFireTime = randomTime();
        }
        return nextFireTime;
    }

    private Date randomTime() {
        Date previousTime = getPreviousFireTime();
        if (previousTime == null) {
            previousTime = new Date();
        }
        long time = previousTime.getTime();
        if ("second".equals(unit)) {
            time = time + RandomUtils.nextInt(0, 60) * 1000;
        } else if ("minute".equals(unit)) {
            time = time + RandomUtils.nextInt(0, 60) * 1000 * 60;
        } else if ("hour".equals(unit)) {
            time = time + RandomUtils.nextInt(0, 60) * 1000 * 60 * 60;
        }
        return new Date(time);
    }
}
