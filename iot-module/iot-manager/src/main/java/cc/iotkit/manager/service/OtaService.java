package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.oss.core.OssClient;
import cc.iotkit.common.oss.factory.OssFactory;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.manager.IDeviceOtaInfoData;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.data.system.ISysOssData;
import cc.iotkit.manager.dto.bo.ota.DeviceOtaInfoBo;
import cc.iotkit.manager.dto.bo.ota.OtaPackageBo;
import cc.iotkit.manager.dto.vo.ota.DeviceOtaInfoVo;
import cc.iotkit.manager.dto.vo.ota.OtaPackageUploadVo;
import cc.iotkit.model.ota.DeviceOtaInfo;
import cc.iotkit.model.ota.OtaPackage;
import cc.iotkit.model.system.SysOss;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import cc.iotkit.common.oss.entity.UploadResult;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

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
    private final DeviceService deviceService;
    private final IDeviceOtaInfoData deviceOtaInfoData;
    private final ISysOssData sysOssData;

    public OtaPackageUploadVo uploadFile(MultipartFile file) throws Exception {
        String originalFileName = file.getOriginalFilename();
        if(originalFileName ==null){
            throw new BizException("文件名为空，获取失败");
        }
        String suffix = StringUtils.substring(originalFileName, originalFileName.lastIndexOf("."), originalFileName.length());
        OssClient storage = OssFactory.instance();
        UploadResult uploadResult;
        try {
            uploadResult = storage.uploadSuffix(file.getBytes(), suffix, file.getContentType());
        } catch (IOException e) {
            throw new BizException(e.getMessage());
        }
        // 保存文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalFileName);
        oss.setService(storage.getConfigKey());
        sysOssData.save(oss);

        String md5 = md5OfFile(file);
        OtaPackageUploadVo otaPackageUploadVo = new OtaPackageUploadVo();
        otaPackageUploadVo.setUrl(uploadResult.getUrl());
        otaPackageUploadVo.setSize(file.getSize());
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
        deviceIds.forEach(deviceId -> {
            deviceService.otaUpgrade(deviceId, true, otaPackage);
        });
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
