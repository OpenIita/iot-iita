package cc.iotkit.manager.service;

import cc.iotkit.data.IOtaPackageData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.ota.OtaPackage;
import cc.iotkit.oss.service.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private void uploadFile(){
        ossTemplate.createBucket("oss02");
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

}
