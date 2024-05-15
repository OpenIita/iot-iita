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
package cc.iotkit.virtualdevice;

import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.VirtualDevice;
import cc.iotkit.model.device.VirtualDeviceLog;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class VirtualExecutor implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        Map<String, Object> data = context.getMergedJobDataMap();
        VirtualManager virtualManager = (VirtualManager) data.get("virtualManager");
        VirtualDevice virtualDevice = (VirtualDevice) data.get("virtualDevice");
        List<DeviceInfo> devices = (List<DeviceInfo>) data.get("devices");
        devices = devices == null ? new ArrayList<>() : devices;
        JobDetail jobDetail = context.getJobDetail();
        String jobKey = jobDetail.getKey().toString();

        VirtualDeviceLog virtualDeviceLog = VirtualDeviceLog.builder()
                .id(UUID.randomUUID().toString())
                .virtualDeviceId(virtualDevice.getId())
                .virtualDeviceName(virtualDevice.getName())
                .deviceTotal(devices.size())
                .result("success")
                .logAt(System.currentTimeMillis())
                .build();

        try {
            for (DeviceInfo device : devices) {
                if (device == null) {
                    continue;
                }
                log.info("invoke virtual device report,jobKey:{},deviceId:{}", jobKey, device.getDeviceId());
                virtualManager.invokeReport(device, virtualDevice.getId());
            }
        } catch (Throwable e) {
            virtualDeviceLog.setResult(e.getMessage());
            log.error("execute job error", e);
        }
        virtualManager.saveLog(virtualDeviceLog);
    }
}
