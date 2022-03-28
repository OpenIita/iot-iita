package cc.iotkit.ruleengine.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceAction implements Action<DeviceActionService.Service> {

    public static final String TYPE = "device";

    private String type;

    private List<DeviceActionService.Service> services;

    private DeviceActionService deviceActionService;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void execute() {
        for (DeviceActionService.Service service : services) {
            deviceActionService.invoke(service);
        }
    }

}
