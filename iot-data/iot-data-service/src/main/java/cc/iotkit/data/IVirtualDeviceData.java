package cc.iotkit.data;

import cc.iotkit.model.Paging;
import cc.iotkit.model.device.VirtualDevice;

import java.util.List;

public interface IVirtualDeviceData extends IOwnedData<VirtualDevice, String> {

    Paging<VirtualDevice> findByUid(String uid, int size, int page);

    List<VirtualDevice> findByUidAndState(String uid, String state);

    List<VirtualDevice> findByTriggerAndState(String trigger, String state);

}
