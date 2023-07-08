package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.data.manager.IDeviceOtaInfoData;
import cc.iotkit.data.manager.IOtaDeviceData;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.manager.dto.bo.ota.DeviceOtaInfoBo;
import cc.iotkit.manager.dto.bo.ota.OtaPackageBo;
import cc.iotkit.manager.dto.vo.ota.DeviceOtaInfoVo;
import cc.iotkit.manager.dto.vo.ota.OtaPackageUploadVo;
import cc.iotkit.model.ota.DeviceOtaInfo;
import cc.iotkit.model.ota.OtaPackage;
import cc.iotkit.oss.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 20:49
 * @Description: oss:
 * region: tb3321.oss-cn-shanghai.aliyuncs.com
 * endpoint: oss-cn-shanghai.aliyuncs.com
 * accessKey: LTAI5tAq5Db5eHt5DTYmXLF4
 * secretKey: bPbHQbeXPSRtyNxxCsw5uRVGJTxNHK
 * buckName: tb3321
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OtaService {

    private final IOtaPackageData iOtaPackageData;
    private final IOtaDeviceData iOtaDeviceData;
    private final DeviceService deviceService;
    private final IDeviceOtaInfoData deviceOtaInfoData;
    private final OssTemplate ossTemplate;


    @Value("${oss.region}")
    private String region;
    @Value("${oss.buckName}")
    private String buckName;

    public OtaPackageUploadVo uploadFile(MultipartFile file, String suffix) throws Exception {
        InputStream inputStream = file.getInputStream();
        long size = file.getSize();
        String objectName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        ossTemplate.putObject(buckName, objectName, inputStream);
        String url = "https://" + region + "/" + objectName;
        String md5 = md5OfFile(file);
        OtaPackageUploadVo otaPackageUploadVo = new OtaPackageUploadVo();
        otaPackageUploadVo.setUrl(url);
        otaPackageUploadVo.setSize(size);
        otaPackageUploadVo.setMd5(md5);
        return otaPackageUploadVo;
    }

    public static String md5OfFile(MultipartFile multipartFile) throws Exception {
        File file = null;
        if (multipartFile.isEmpty()) {
            return "";
        }
        try {
            //本质上还是在项目根路径创建文件
            file = new File(multipartFile.getOriginalFilename());
            //将MultipartFile的byte[]写入到file中
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
            byte[] bytes = DigestUtils.md5(multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fs = new FileInputStream(file);
        BufferedInputStream bs = new BufferedInputStream(fs);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = bs.read(buffer, 0, buffer.length)) != -1) {
            md.update(buffer, 0, bytesRead);
        }
        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte bite : digest) {
            sb.append(String.format("%02x", bite & 0xff));
        }
        return sb.toString();
    }

    public OtaPackage addOtaPackage(OtaPackageBo otaPackage) {
        return iOtaPackageData.save(otaPackage.to(OtaPackage.class));
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
        deviceIds.forEach(deviceId -> deviceService.otaUpgrade(deviceId, true, otaPackage));
    }

    public Paging<DeviceOtaInfoVo> otaResult(PageRequest<DeviceOtaInfoBo> request) {
        return deviceOtaInfoData.findAll(request.to(DeviceOtaInfo.class)).to(DeviceOtaInfoVo.class);
    }

    public void testStartUpgrade() {
        String deviceId = "16885697173790test100001230000123";
        OtaPackage otaPackage = OtaPackage.builder()
                .createAt(System.currentTimeMillis())
                .desc("升级测试")
                .md5("AAAABCC")
                .sign("AAAAAAAA")
                .isDiff(false)
                .size(1024L)
                .url("http://www.baidu.com/resource/test.jpg")
                .version("1.2.1")
                .build();
        deviceService.otaUpgrade(deviceId, true, otaPackage);
    }

}
