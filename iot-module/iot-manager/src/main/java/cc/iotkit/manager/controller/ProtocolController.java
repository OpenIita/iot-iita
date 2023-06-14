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
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.manager.dto.bo.ChangeStateBo;
import cc.iotkit.manager.dto.bo.protocolcomponent.ProtocolComponentBo;
import cc.iotkit.manager.dto.bo.protocolconverter.ProtocolConverterBo;
import cc.iotkit.manager.dto.vo.protocolcomponent.ProtocolComponentVo;
import cc.iotkit.manager.dto.vo.protocolconverter.ProtocolConverterVo;
import cc.iotkit.manager.service.IProtocolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public boolean addComponent(@RequestBody @Validated Request<ProtocolComponentBo> bo) {
        return protocolService.addComponent(bo.getData());
    }

    @ApiOperation("修改组件")
    @PostMapping("/editComponent")
    public String saveComponent(@RequestBody @Validated Request<ProtocolComponentBo> bo) {
       return protocolService.saveComponent(bo.getData());
    }

    @ApiOperation("获取组件详情")
    @PostMapping("/getComponentDetail")
    public ProtocolComponentVo getComponentScript(@Validated @RequestBody Request<String> req) {
        String id = req.getData();
        return protocolService.getProtocolComponent(id);

    }

    @ApiOperation("保存组件脚本")
    @PostMapping("/saveComponentScript")
    public boolean saveComponentScript(@Validated
            @RequestBody Request<ProtocolComponentBo> upReq) {
        return protocolService.saveComponentScript(upReq.getData());
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
    @PostMapping("/converter/add")
    public boolean addConverter(@Validated @RequestBody Request<ProtocolConverterBo> converter) {
        return protocolService.addConverter(converter.getData());
    }

    @ApiOperation("修改转换脚本")
    @PostMapping("/converter/edit")
    public boolean editConverter(@Validated @RequestBody Request<ProtocolConverterBo> req) {
        return protocolService.editConverter(req.getData());
    }



    @ApiOperation("获取转换脚本详情")
    @PostMapping("/getConverterScript")
    public ProtocolConverterVo getConverter(@RequestBody Request<String> req) {
        String id = req.getData();

        return protocolService.getConverter(id);

    }

    @PostMapping("/converterScript/edit")
    @ApiOperation("保存转换脚本")
    public boolean saveConverterScript(
            @Validated @RequestBody Request<ProtocolConverterBo> req) {

        return protocolService.saveConverterScript(req.getData());
    }

    @PostMapping("/converter/delete")
    @ApiOperation("删除转换脚本")
    public boolean deleteConverter(@RequestBody @Validated Request<String> req) {
        String id = req.getData();
       return protocolService.deleteConverter(id);
    }

    @PostMapping("/component/changeState}")
    @ApiOperation("组件启用/禁用")
    public boolean changeComponentState(@RequestBody @Validated Request<ChangeStateBo> req) {
       return protocolService.changeComponentState(req.getData());
    }

}
