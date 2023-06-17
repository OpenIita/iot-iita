package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.manager.dto.bo.ota.DeviceOtaInfoBo;
import cc.iotkit.manager.dto.bo.ota.DeviceUpgradeBo;
import cc.iotkit.manager.dto.vo.ota.DeviceOtaInfoVO;
import cc.iotkit.manager.service.OtaService;
import cc.iotkit.model.alert.AlertConfig;
import cc.iotkit.model.ota.DeviceOta;
import cc.iotkit.model.ota.OtaPackage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.InputStream;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 20:42
 * @Description:
 */
@Api(tags = {"ota升级管理"})
@Slf4j
@RestController
@RequestMapping("/ota")
public class OtaController {

    @Resource
    private OtaService otaService;

    @ApiOperation("升级包上传")
    @PostMapping("/package/upload")
    public String packageUpload(MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffix = StringUtils.isEmpty(fileName) ? "" : fileName.substring(fileName.lastIndexOf("."));
            InputStream ins = file.getInputStream();
            return otaService.uploadFile(ins, suffix);
        }
        return "";
    }

    @ApiOperation("新增升级包")
    @PostMapping("/package/add")
    public OtaPackage addChannelTemplate(@RequestBody @Valid Request<OtaPackage> request) throws Exception {
        return otaService.addOtaPackage(request.getData());
    }

    @ApiOperation("删除升级包")
    @PostMapping("/package/delById")
    public Boolean delChannelConfigById(@RequestBody @Valid Request<String> request) {
        return otaService.delOtaPackageById(request.getData());
    }

    @ApiOperation("升级包列表")
    @PostMapping("/package/getList")
    public Paging<OtaPackage> packageList(@RequestBody @Validated PageRequest<OtaPackage> request) {
        return otaService.getOtaPackagePageList(request);
    }

    @ApiOperation("设备升级")
    @PostMapping("/device/upgrade")
    public String deviceUpgrade(@RequestBody Request<DeviceUpgradeBo> request) {
        return otaService.startUpgrade(request.getData().getOtaId(), request.getData().getDeviceId());
    }

    @ApiOperation("设备升级结果查询")
    @PostMapping("/result")
    public Paging<DeviceOtaInfoVO> otaResult(@RequestBody PageRequest<DeviceOtaInfoBo> request) {
        return otaService.otaResult(request);
    }

    @ApiOperation("OTA升级")
    @PostMapping("/down")
    public void ota(@RequestBody PageRequest<DeviceOtaInfoBo> request) {
        otaService.startUpgrade("0417d633-2806-4a0f-ab2a-afe35e2b2dc6", "16870054884740abcd123456000000103");
    }


}
