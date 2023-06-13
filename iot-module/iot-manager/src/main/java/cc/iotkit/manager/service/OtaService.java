package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.comps.ApiTool;
import cc.iotkit.data.manager.IOtaDeviceData;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.model.alert.AlertConfig;
import cc.iotkit.model.ota.OtaPackage;
import com.google.gson.JsonObject;
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
    public void startUpgrade(String otaId, String deviceId) {
        OtaPackage otaPackage = iOtaPackageData.findById(otaId);
        //构建升级包
        JsonObject buildOtaPackage = buildOtaPackage(otaPackage);
        String id = deviceService.otaUpgrade(deviceId, true, buildOtaPackage);
    }

    private JsonObject buildOtaPackage(OtaPackage otaPackage) {
        JsonObject ota = new JsonObject();
        JsonObject extData = new JsonObject();
        extData.addProperty("key1", "测试1");
        extData.addProperty("key2", "测试2");
        ota.addProperty("size", otaPackage.getSize());
        ota.addProperty("sign", otaPackage.getSign());
        ota.addProperty("version", otaPackage.getVersion());
        ota.addProperty("isDiff", Boolean.toString(otaPackage.getIsDiff()));
        ota.addProperty("url", otaPackage.getUrl());
        ota.addProperty("signMethod", "MD5");
        ota.addProperty("module", "MCU");
        ota.add("extData", extData);
        return ota;
    }

}
