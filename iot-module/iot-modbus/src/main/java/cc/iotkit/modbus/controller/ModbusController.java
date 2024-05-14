/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.modbus.controller;


import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.excel.utils.ExcelUtil;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.modbus.dto.bo.modbus.ModbusInfoBo;
import cc.iotkit.modbus.dto.bo.modbus.ModbusThingModelBo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusInfoVo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusThingModelImportVo;
import cc.iotkit.modbus.dto.vo.modbus.ModbusThingModelVo;
import cc.iotkit.modbus.service.IModbusInfoService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * @Description: Modbus模版管理
 * @Author: ZOUZDC
 * @Date: 2024/4/28 23:10
 */
@Api(tags = {"modbus"})
@Slf4j
@RestController
@RequestMapping("/modbus")
public class ModbusController {

    @Autowired
    private IModbusInfoService modbusInfoService;


    @ApiOperation(value = "ModbusInfo模版列表", notes = "设备列表", httpMethod = "POST")
    @SaCheckPermission("iot:modbus:list")
    @PostMapping("/list")
    public Paging<ModbusInfoVo> getDevices(@Validated @RequestBody PageRequest<ModbusInfoBo> request) {
        return modbusInfoService.selectPageList(request);
    }


    @ApiOperation("新建ModbusInfo")
    @SaCheckPermission("iot:modbus:add")
    @PostMapping(value = "/add")
    @Log(title = "modbusInfo", businessType = BusinessType.INSERT)
    public ModbusInfoVo create(@Validated(AddGroup.class) @RequestBody Request<ModbusInfoBo> request) {
        return modbusInfoService.addEntity(request.getData());
    }

    @ApiOperation(value = "编辑ModbusInfo")
    @SaCheckPermission("iot:modbus:edit")
    @PostMapping("/edit")
    @Log(title = "modbusInfo", businessType = BusinessType.UPDATE)
    public boolean edit(@Validated(EditGroup.class) @RequestBody Request<ModbusInfoBo> request) {
        return modbusInfoService.updateEntity(request.getData());
    }

    @ApiOperation("查看ModbusInfo详情")
    @SaCheckPermission("iot:modbus:query")
    @PostMapping(value = "/getDetail")
    public ModbusInfoVo getDetail(@RequestBody @Validated Request<Long> request) {
        return modbusInfoService.getDetail(request.getData());
    }

    @ApiOperation("删除ModbusInfo")
    @SaCheckPermission("iot:modbus:remove")
    @PostMapping(value = "/deleteModbus")
    public boolean deleteProduct(@RequestBody @Validated Request<Long> request) {
        return modbusInfoService.deleteModbus(request.getData());
    }

    /**
     * 导入点位模型
     */
    @ApiOperation(value = "导入点位模型")
    @SaCheckPermission("iot:modbus:add")
    @PostMapping("/importData")
    public String importData(@RequestPart("file") MultipartFile file, @RequestParam("productKey") String productKey) {
        if(StrUtil.isBlank(productKey)){
           throw new BizException("缺少productKey");
        }

        return modbusInfoService.importData(file,productKey);
    }
    @ApiOperation("下载点位模版")
    @SaCheckPermission("iot:modbus:query")
    @PostMapping("/exportData")
    public void exportDeviceTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "点位模版", ModbusThingModelImportVo.class, response);
    }


    @ApiOperation("查看点位物模型")
    @SaCheckPermission("iot:modbus:query")
    @PostMapping("/getThingModelByProductKey")
    public ModbusThingModelVo getThingModelByProductKey(@RequestBody @Validated Request<String> request) {
        return modbusInfoService.getThingModelByProductKey(request.getData());
    }

    @ApiOperation("保存点位物模型")
    @SaCheckPermission("iot:modbus:edit")
    @PostMapping("/thingModel/save")
    public boolean saveThingModel(@Validated @RequestBody Request<ModbusThingModelBo> request) {
        return modbusInfoService.saveThingModel(request.getData());
    }


    @ApiOperation("同步点位物模型到产品")
    @SaCheckPermission("iot:modbus:edit")
    @PostMapping("/syncToProduct")
    public boolean syncToProduct(@Validated @RequestBody Request<ModbusThingModelBo> request) {
        return modbusInfoService.syncToProduct(request.getData());
    }



}
