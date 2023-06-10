package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.comps.ApiTool;
import cc.iotkit.data.manager.IOtaDeviceData;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.model.alert.AlertConfig;
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
    private final DeviceService deviceService;


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

    public Paging<OtaPackage> getOtaPackagePageList(PageRequest<OtaPackage> request) {
        return iOtaPackageData.findAll(request);
    }

    public void batchOta() {
    }

    /**
     * 开始升级
     */
    public void startUpgrade() {
        //构建升级包
        deviceService.otaUpgrade("", true);
    }

}
