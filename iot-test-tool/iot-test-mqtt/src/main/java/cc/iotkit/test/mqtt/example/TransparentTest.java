/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.test.mqtt.example;

import cc.iotkit.test.mqtt.config.Mqtt;
import cc.iotkit.test.mqtt.model.Request;
import cc.iotkit.test.mqtt.service.Gateway;
import cc.iotkit.test.mqtt.service.ReportTask;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 透传测试
 */
@Slf4j
public class TransparentTest {

    public static void main(String[] args) throws IOException {


        if (args.length == 0) {
            Mqtt.brokerHost = "127.0.0.1";
//            Mqtt.brokerHost = "120.76.96.206";
//            Mqtt.brokerHost = "172.16.1.109";
        } else {
            Mqtt.brokerHost = args[0];
        }

        log.info("start gateway ");
        Gateway gateway = new Gateway("hbtgIA0SuVw9lxjB", "xdkKUymrEGSCYWswqCvSPyRSFvH5j7CU",
                "TEST:GW:T0001");

        gateway.addSubDevice("hbtgIA0SuVw9lxjB", "xdkKUymrEGSCYWswqCvSPyRSFvH5j7CU",
                "TEST_LIGHT_0001",
                "M1");

        gateway.onDeviceOnline(device -> {
            String pk = device.getProductKey();

            //设备上线后添加上报定时任务
            ReportTask reportTask = new ReportTask(gateway.getClient());
            reportTask.addTask(String.format("/sys/%s/%s/s/event/rawReport",
                            pk, device.getDeviceName()),
                    () -> {
                        Request request = new Request();
                        request.setId(UUID.randomUUID().toString());
                        request.setMethod("thing.event.rawReport");
                        Map<String, Object> param = new HashMap<>();
                        param.put("model", "M1");
                        param.put("deviceName", "TEST_LIGHT_0001");
                        param.put("data", "111110011");
                        request.setParams(param);
                        return request;
                    });
            reportTask.start(10);
        });

        gateway.start();

        System.in.read();
    }
}
