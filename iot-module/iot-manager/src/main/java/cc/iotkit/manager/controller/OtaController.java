package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.manager.dto.bo.ota.DeviceOtaInfoBo;
import cc.iotkit.manager.dto.bo.ota.DeviceUpgradeBo;
import cc.iotkit.manager.dto.bo.ota.OtaPackageBo;
import cc.iotkit.manager.dto.vo.ota.DeviceOtaInfoVo;
import cc.iotkit.manager.dto.vo.ota.OtaPackageUploadVo;
import cc.iotkit.manager.service.OtaService;
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
    public OtaPackageUploadVo packageUpload(MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffix = StringUtils.isEmpty(fileName) ? "" : fileName.substring(fileName.lastIndexOf("."));
            return otaService.uploadFile(file, suffix);
        }
        return null;
    }

    @ApiOperation("新增升级包")
    @PostMapping("/package/add")
    public OtaPackage packageAdd(@RequestBody @Valid Request<OtaPackageBo> request) {
        return otaService.addOtaPackage(request.getData());
    }

    @ApiOperation("删除升级包")
    @PostMapping("/package/delById")
    public Boolean delPackageById(@RequestBody @Valid Request<Long> request) {
        return otaService.delOtaPackageById(request.getData());
    }

    @ApiOperation("升级包列表")
    @PostMapping("/package/getList")
    public Paging<OtaPackage> packageList(@RequestBody @Validated PageRequest<OtaPackage> request) {
        return otaService.getOtaPackagePageList(request);
    }

    @ApiOperation("OTA升级")
    @PostMapping("/device/upgrade")
    public void deviceUpgrade(@RequestBody Request<DeviceUpgradeBo> request) {
        otaService.startUpgrade(request.getData().getOtaId(), request.getData().getDeviceIds());
    }

    @ApiOperation("设备升级结果查询")
    @PostMapping("/result")
    public Paging<DeviceOtaInfoVo> otaResult(@RequestBody PageRequest<DeviceOtaInfoBo> request) {
        return otaService.otaResult(request);
    }

    @ApiOperation("ota升级测试")
    @PostMapping("/testStartUpgrade")
    public void testStartUpgrade(@RequestBody Request<Void> request) {
        otaService.testStartUpgrade();
    }

}
