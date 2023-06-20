package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.comps.ApiTool;
import cc.iotkit.data.manager.IDeviceOtaInfoData;
import cc.iotkit.data.manager.IOtaDeviceData;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.data.service.DeviceOtaInfoDataImpl;
import cc.iotkit.manager.dto.bo.ota.DeviceOtaInfoBo;
import cc.iotkit.manager.dto.vo.channel.ChannelTemplateVo;
import cc.iotkit.manager.dto.vo.ota.DeviceOtaInfoVO;
import cc.iotkit.model.alert.AlertConfig;
import cc.iotkit.model.notify.ChannelTemplate;
import cc.iotkit.model.ota.DeviceOtaInfo;
import cc.iotkit.model.ota.OtaPackage;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.InputStream;
import java.util.List;
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
    private final IDeviceOtaInfoData deviceOtaInfoData;


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

    public Boolean delOtaPackageById(Long id) {
        iOtaPackageData.deleteById(id);
        return Boolean.TRUE;
    }

    public Paging<OtaPackage> getOtaPackagePageList(PageRequest<OtaPackage> request) {
        return iOtaPackageData.findAll(request);
    }

    /**
     * 开始升级
     */
    public void startUpgrade(Long otaId, List<String> deviceIds) {
        OtaPackage otaPackage = iOtaPackageData.findById(otaId);
        deviceIds.forEach(deviceId -> {
            deviceService.otaUpgrade(deviceId, true, otaPackage);
        });
    }

    public Paging<DeviceOtaInfoVO> otaResult(PageRequest<DeviceOtaInfoBo> request) {
        return deviceOtaInfoData.findAll(request.to(DeviceOtaInfo.class)).to(DeviceOtaInfoVO.class);
    }

}
