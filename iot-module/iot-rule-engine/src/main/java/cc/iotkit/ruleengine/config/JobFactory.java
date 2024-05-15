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
package cc.iotkit.ruleengine.config;

import cc.iotkit.model.rule.TaskInfo;
import cc.iotkit.ruleengine.task.ActionExecutorManager;
import cc.iotkit.ruleengine.task.CommonJob;
import cc.iotkit.ruleengine.task.TaskManager;
import org.quartz.JobDetail;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

@Component
public class JobFactory extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;
    @Autowired
    private ActionExecutorManager actionExecutorManager;
    @Autowired
    private TaskManager taskManager;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        JobDetail jobDetail = bundle.getJobDetail();
        Object objTask = jobDetail.getJobDataMap().get("taskInfo");

        Class<?> clz = jobDetail.getJobClass();
        Object jobInstance;
        if (clz == CommonJob.class && objTask instanceof TaskInfo) {
            jobInstance = new CommonJob(actionExecutorManager, taskManager, (TaskInfo) objTask);
        } else {
            jobInstance = super.createJobInstance(bundle);
        }
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}