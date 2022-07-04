/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.test.mqtt;

import cc.iotkit.test.mqtt.config.Mqtt;
import cc.iotkit.test.mqtt.service.Gateway;

import java.io.IOException;

public class Simulator {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
//            Mqtt.brokerHost = "127.0.0.1";
            Mqtt.brokerHost = "120.76.96.206";
        } else {
            Mqtt.brokerHost = args[0];
        }

        new Thread(() -> {
            Gateway gateway = new Gateway("hbtgIA0SuVw9lxjB", "AA:BB:CC:DD:22");
            gateway.addSubDevice("Rf4QSjbm65X45753", "ABC12300002", "S01");
//            gateway.addSubDevice("Rf4QSjbm65X45753", "ABC12300003", "S01");
//            gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300001", "F01");
//            gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300002", "F01");
//            gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300002", "F01");
            gateway.start();
//
//            Gateway gateway2 = new Gateway("N523nWsCiG3CAn6X", "AA:BB:CC:EE:01");
//            //插座
//            gateway2.addSubDevice("cGCrkK7Ex4FESAwe", "ABE12300001", "S1");
//            gateway2.addSubDevice("cGCrkK7Ex4FESAwe", "ABE12300002", "S1");
//            gateway2.addSubDevice("6kYp6jszrDns2yh4", "ABE12400001", "S1");
//            gateway2.addSubDevice("", "ABE12500001", "M1");
//            gateway2.start();
        }).start();

        System.in.read();
    }
}
