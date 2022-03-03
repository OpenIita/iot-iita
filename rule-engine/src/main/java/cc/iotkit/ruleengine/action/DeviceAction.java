package cc.iotkit.ruleengine.action;

import cc.iotkit.deviceapi.IDeviceService;
import cc.iotkit.deviceapi.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceAction implements Action<Service> {

    public static final String TYPE = "device";

    private String type;

    private List<Service> services;

    private IDeviceService deviceService;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void execute() {
        for (Service service : services) {
            deviceService.invoke(service);
        }
    }

}
