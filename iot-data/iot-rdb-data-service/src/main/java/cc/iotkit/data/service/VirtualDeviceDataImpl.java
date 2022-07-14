package cc.iotkit.data.service;

import cc.iotkit.data.IVirtualDeviceData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.VirtualDevice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VirtualDeviceDataImpl implements IVirtualDeviceData {
    @Override
    public List<VirtualDevice> findByUid(String uid) {
        return null;
    }

    @Override
    public Paging<VirtualDevice> findByUid(String uid, int size, int page) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public List<VirtualDevice> findByUidAndState(String uid, String state) {
        return null;
    }

    @Override
    public List<VirtualDevice> findByTriggerAndState(String trigger, String state) {
        return new ArrayList<>();
    }

    @Override
    public VirtualDevice findById(String s) {
        return null;
    }

    @Override
    public VirtualDevice save(VirtualDevice data) {
        return null;
    }

    @Override
    public VirtualDevice add(VirtualDevice data) {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<VirtualDevice> findAll() {
        return null;
    }

    @Override
    public Paging<VirtualDevice> findAll(int page, int size) {
        return null;
    }
}
