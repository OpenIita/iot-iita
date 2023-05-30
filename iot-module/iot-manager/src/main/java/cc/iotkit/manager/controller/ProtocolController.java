/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.comps.config.ComponentConfig;
import cc.iotkit.data.manager.IProtocolComponentData;
import cc.iotkit.data.manager.IProtocolConverterData;
import cc.iotkit.data.manager.IUserInfoData;
import cc.iotkit.manager.dto.bo.ChangeStateBo;
import cc.iotkit.manager.dto.bo.protocolcomponent.ProtocolComponentBo;
import cc.iotkit.manager.dto.bo.protocolconverter.ProtocolConverterBo;
import cc.iotkit.manager.dto.vo.protocolcomponent.ProtocolComponentVo;
import cc.iotkit.manager.dto.vo.protocolconverter.ProtocolConverterVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.common.api.Paging;
import cc.iotkit.manager.service.IProtocolService;
import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.protocol.ProtocolConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Api(tags = {"协议"})
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Autowired
    private IProtocolService protocolService;

    @ApiOperation("上传Jar包")
    @PostMapping("/uploadJar")
    public String uploadJar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") String id) {
       return protocolService.uploadJar(file, id);
    }

    @ApiOperation("添加组件")
    @PostMapping("/addComponent")
    public boolean addComponent(ProtocolComponentBo component) {
        return protocolService.addComponent(component);
    }

    @ApiOperation("修改组件")
    @PostMapping("/editComponent")
    public String saveComponent(ProtocolComponentBo component) {
       return protocolService.saveComponent(component);
    }

    @ApiOperation("获取组件详情")
    @PostMapping("/getComponentDetail")
    public ProtocolComponentVo getComponentScript(@Validated @RequestBody Request<String> req) {
        String id = req.getData();
        return protocolService.getProtocolComponent(id);

    }

    @ApiOperation("保存组件脚本")
    @PostMapping("/saveComponentScript")
    public boolean saveComponentScript(
            @RequestBody ProtocolComponentBo upReq) {
        return protocolService.saveComponentScript(upReq);
    }


    @ApiOperation("删除组件")
    @PostMapping("/delete")
    public boolean deleteComponent(@Validated @RequestBody Request<String> req) {
        return protocolService.deleteComponent(req.getData());
    }

    @ApiOperation("获取组件列表")
    @PostMapping("/list")
    public Paging<ProtocolComponentVo> getComponents(@Validated @RequestBody
            PageRequest<ProtocolComponentBo> query) {
        return protocolService.selectPageList(query);
    }

    @ApiOperation("获取转换脚本列表")
    @PostMapping("/converters/list")
    public Paging<ProtocolConverterVo> getConverters(@Validated @RequestBody PageRequest<ProtocolConverterBo> query) {
        return protocolService.selectConvertersPageList(query);
    }

    @ApiOperation("新增转换脚本")
    @PostMapping("/addConverter")
    public boolean addConverter(@Validated @RequestBody ProtocolConverterBo converter) {
        return protocolService.addConverter(converter);

    }

    @ApiOperation("修改转换脚本")
    @PostMapping("/editConverter")
    public boolean editConverter(ProtocolConverterBo req) {
        return protocolService.editConverter(req);
    }



    @ApiOperation("获取转换脚本详情")
    @PostMapping("/getConverterScript/{id}")
    public ProtocolConverterVo getConverter(@RequestBody Request<String> req) {
        String id = req.getData();

        return protocolService.getConverter(id);

    }

    @PostMapping("/saveConverterScript")
    public boolean saveConverterScript(
            @Validated @RequestBody ProtocolConverterBo req) {

        return protocolService.saveConverterScript(req);
    }

    @PostMapping("/deleteConverter")
    public boolean deleteConverter(@RequestBody @Validated Request<String> req) {
        String id = req.getData();
       return protocolService.deleteConverter(id);
    }

    @PostMapping("/component/{id}/state/{state}")
    public boolean changeComponentState(@RequestBody @Validated ChangeStateBo req) {
       return protocolService.changeComponentState(req);
    }

}
