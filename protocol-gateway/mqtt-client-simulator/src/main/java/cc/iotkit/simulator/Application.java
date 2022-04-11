package cc.iotkit.simulator;

import cc.iotkit.simulator.config.Mqtt;
import cc.iotkit.simulator.service.Gateway;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            Mqtt.broker = "tcp://127.0.0.1:1883";
//            Mqtt.broker = "tcp://120.76.96.206:1883";
        } else {
            Mqtt.broker = args[0];
        }
        new Thread(() -> {
            Gateway gateway = new Gateway("hbtgIA0SuVw9lxjB", "AA:BB:CC:DD:22");
            gateway.addSubDevice("Rf4QSjbm65X45753", "ABC12300002", "S01");
            gateway.addSubDevice("Rf4QSjbm65X45753", "ABC12300003", "S01");
            gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300001", "F01");
            gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300002", "F01");
            gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300002", "F01");
            gateway.start();

            Gateway gateway2 = new Gateway("N523nWsCiG3CAn6X", "AA:BB:CC:EE:01");
            //插座
            gateway2.addSubDevice("cGCrkK7Ex4FESAwe", "ABE12300001", "S1");
            gateway2.addSubDevice("cGCrkK7Ex4FESAwe", "ABE12300002", "S1");
            gateway2.start();
        }).start();

        System.in.read();
    }
}
