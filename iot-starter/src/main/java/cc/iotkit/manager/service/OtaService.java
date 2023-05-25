package cc.iotkit.manager.service;

import cc.iotkit.data.IOtaPackageData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.ota.OtaPackage;
import cc.iotkit.oss.service.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 20:49
 * @Description:
 */
@Slf4j
@Service
public class OtaService {

    @Resource
    private IOtaPackageData iOtaPackageData;
    @Resource
    private OssTemplate ossTemplate;
    @Value("${oss.region}")
    private String region;

    public String uploadFile(InputStream inputStream, String suffix) throws Exception {
        String buckName = "tb3321";
        String objectName = UuidUtil.getTimeBasedUuid().toString() + suffix;
        ossTemplate.putObject(buckName, objectName, inputStream);
        return "https://" + region + "/" + objectName;
    }

    public OtaPackage addOtaPackage(OtaPackage otaPackage) {
        return iOtaPackageData.add(otaPackage);
    }

    public Paging<OtaPackage> getOtaPackagePageList(int page, int size) {
        return iOtaPackageData.findAll(page, size);
    }

    public Boolean delOtaPackageById(String id) {
        iOtaPackageData.deleteById(id);
        return Boolean.TRUE;
    }

    public void findByVersionGreaterThan(String version, String deviceId) {
        iOtaPackageData.findByVersionGreaterThan(version);
    }

}
