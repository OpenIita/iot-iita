package cc.iotkit.manager.service;

import cc.iotkit.data.manager.IOtaDeviceData;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.model.ota.OtaPackage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 20:49
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OtaService {

    private final IOtaPackageData iOtaPackageData;
    private final IOtaDeviceData iOtaDeviceData;


    @Value("${oss.region}")
    private String region;
    @Value("${oss.buckName}")
    private String buckName;

    public String uploadFile(InputStream inputStream, String suffix) throws Exception {
        String objectName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        return "https://" + region + "/" + objectName;
    }

    public OtaPackage addOtaPackage(OtaPackage otaPackage) {
        return iOtaPackageData.save(otaPackage);
    }

    public Boolean delOtaPackageById(String id) {
        iOtaPackageData.deleteById(id);
        return Boolean.TRUE;
    }

    public void findByVersionGreaterThan(String version, String deviceId) {
        iOtaPackageData.findByVersionGreaterThan(version);
    }

    public void batchOta() {
    }

}
