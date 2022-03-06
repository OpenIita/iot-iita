package cc.iotkit.simulator;

import cc.iotkit.simulator.config.Mqtt;
import cc.iotkit.simulator.service.Gateway;

public class Application {

    public static void main(String[] args) {

        Mqtt.broker = args[0];

        Gateway gateway = new Gateway("hbtgIA0SuVw9lxjB", "AA:BB:CC:DD:22");
        gateway.addSubDevice("Rf4QSjbm65X45753", "ABC12300002", "S01");
        gateway.addSubDevice("Rf4QSjbm65X45753", "ABC12300003", "S01");
        gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300001", "F01");
        gateway.addSubDevice("hdX3PCMcFrCYpesJ", "ABD12300002", "F01");
        gateway.start();
    }
}
