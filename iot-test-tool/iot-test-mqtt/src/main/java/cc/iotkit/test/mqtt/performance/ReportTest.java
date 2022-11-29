/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.test.mqtt.performance;

import cc.iotkit.test.mqtt.config.Mqtt;
import cc.iotkit.test.mqtt.model.Request;
import cc.iotkit.test.mqtt.service.Gateway;
import cc.iotkit.test.mqtt.service.ReportTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 上报压力测试
 */
@Slf4j
public class ReportTest {

    public static void main(String[] args) throws IOException {


        if (args.length == 0) {
//            Mqtt.brokerHost = "127.0.0.1";
            Mqtt.brokerHost = "120.76.96.206";
//            Mqtt.brokerHost = "172.16.1.109";
        } else {
            Mqtt.brokerHost = args[0];
        }

        int total = 10;
        if (args.length > 1) {
            total = Integer.parseInt(args[1]);
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < total; i++) {
            int finalI = i;
            executor.submit(() -> {
                log.info("start gateway " + (finalI + 1));
                Gateway gateway = new Gateway("hbtgIA0SuVw9lxjB",
                        "TEST:GW:" + StringUtils.leftPad(finalI + "", 6, "0"));

                gateway.addSubDevice("Rf4QSjbm65X45753",
                        "TEST_SW_" + StringUtils.leftPad(finalI + "", 6, "0"),
                        "S01");

                gateway.addSubDevice("cGCrkK7Ex4FESAwe",
                        "TEST_SC_" + StringUtils.leftPad(finalI + "", 6, "0"),
                        "S01");

                gateway.onDeviceOnline((device) -> {
                    String pk = device.getProductKey();
                    if (!"Rf4QSjbm65X45753".equals(pk)) {
                        return;
                    }

                    //设备上线后添加上报定时任务
                    ReportTask reportTask = new ReportTask(gateway.getClient());
                    reportTask.addTask(String.format("/sys/%s/%s/s/event/property/post",
                            pk, device.getDeviceName()),
                            () -> {
                                Request request = new Request();
                                request.setId(UUID.randomUUID().toString());
                                request.setMethod("thing.event.property.post");
                                Map<String, Object> param = new HashMap<>();
                                param.put("volt", Math.round(Math.random() * 100));
                                request.setParams(param);
                                return request;
                            });
                    reportTask.start(10);
                });

                gateway.start();
            });
        }

        System.in.read();
    }
}
