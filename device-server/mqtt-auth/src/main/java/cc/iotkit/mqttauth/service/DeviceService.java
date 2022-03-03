package cc.iotkit.mqttauth.service;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.mqttauth.dao.DeviceDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    public DeviceInfo register(DeviceInfo device) {
        DeviceInfo deviceInfo = deviceDao.getByPkAndDn(device.getProductKey(), device.getDeviceName());
        if (deviceInfo != null) {
            device.setDeviceId(deviceInfo.getDeviceId());
            deviceDao.updateDevice(device);
            log.info("device register update:{}", JsonUtil.toJsonString(device));
            return device;
        }

        device.setDeviceId(newDeviceId(device.getDeviceName()));
        deviceDao.addDevice(device);
        log.info("device registered:{}", JsonUtil.toJsonString(device));
        return device;
    }

    public DeviceInfo getByPkAndDn(String pk, String dn) {
        return deviceDao.getByPkAndDn(pk, dn);
    }

    public void online(String pk, String dn) {
        DeviceInfo device = new DeviceInfo();
        device.setProductKey(pk);
        device.setDeviceName(dn);

        device.getState().setOnline(true);
        device.getState().setOnlineTime(System.currentTimeMillis());
        deviceDao.updateDeviceByPkAndDn(device);
    }

    /**
     * 1-13位	时间戳
     * 14-29位	deviceNae，去除非字母和数字，不足16位补0，超过16位的mac取后16位，共16位
     * 30-31位	mac长度，共2位
     * 32位	随机一个0-f字符
     */
    public static String newDeviceId(String deviceNae) {
        int maxDnLen = 16;
        String dn = deviceNae.replaceAll("[^0-9A-Za-z]", "");
        if (dn.length() > maxDnLen) {
            dn = dn.substring(dn.length() - maxDnLen);
        } else {
            dn = (dn + "00000000000000000000").substring(0, maxDnLen);
        }
        String len = StringUtils.leftPad(deviceNae.length() + "", 2, '0');
        String rnd = Integer.toHexString(RandomUtils.nextInt(0, 16));
        return (System.currentTimeMillis() + "0" + dn + len + rnd).toLowerCase();
    }

}
