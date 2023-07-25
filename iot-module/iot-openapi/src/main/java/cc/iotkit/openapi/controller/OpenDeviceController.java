package cc.iotkit.openapi.controller;

import cc.iotkit.common.api.Request;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.openapi.dto.bo.device.OpenapiDeviceBo;
import cc.iotkit.openapi.service.OpenBaseService;
import cc.iotkit.openapi.service.OpenDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"openapi-设备"})
@Slf4j
@RestController
@RequestMapping("/openapi/v1/device")
public class OpenDeviceController {

    @Autowired
    private OpenBaseService openBaseService;

    @Autowired
    private OpenDeviceService openDeviceService;

    @ApiOperation("获取设备详情")
    @PostMapping("/detail")
    public DeviceInfo getDetail(@RequestBody @Validated Request<OpenapiDeviceBo> request) {
        return openDeviceService.getDetail(request.getData());
    }

}
