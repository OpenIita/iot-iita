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
import cc.iotkit.test.mqtt.service.Gateway;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 连接压力测试
 */
@Slf4j
public class ConnectionTest {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            Mqtt.brokerHost = "127.0.0.1";
//            Mqtt.brokerHost = "120.76.96.206";
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
                Gateway gateway = new Gateway("hbtgIA0SuVw9lxjB","xdkKUymrEGSCYWswqCvSPyRSFvH5j7CU",
                        "TEST:GW:T" + StringUtils.leftPad(finalI + "", 6, "0"));

//                gateway.addSubDevice("Rf4QSjbm65X45753",
//                        "TEST_SW_" + StringUtils.leftPad(finalI + "", 6, "0"),
//                        "S01");
//
//                gateway.addSubDevice("cGCrkK7Ex4FESAwe",
//                        "TEST_SC_" + StringUtils.leftPad(finalI + "", 6, "0"),
//                        "S01");
//
//                gateway.addSubDevice("xpsYHExTKPFaQMS7",
//                        "TEST_LT_" + StringUtils.leftPad(finalI + "", 6, "0"),
//                        "L01");

                gateway.start();
            });
        }

        System.in.read();
    }
}
