package cc.iotkit.temporal.es.service;

import cc.iotkit.model.Paging;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.temporal.IVirtualDeviceLogData;
import org.springframework.stereotype.Service;

@Service
public class VirtualDeviceLogDataImpl implements IVirtualDeviceLogData {
    @Override
    public Paging<VirtualDeviceLog> findByVirtualDeviceId(String virtualDeviceId, int page, int size) {
        return null;
    }

    @Override
    public void add(VirtualDeviceLog log) {

    }
}
