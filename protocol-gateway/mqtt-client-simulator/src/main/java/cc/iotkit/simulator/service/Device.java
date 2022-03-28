package cc.iotkit.simulator.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    protected String productKey;

    protected String deviceName;

    private String model;
}
