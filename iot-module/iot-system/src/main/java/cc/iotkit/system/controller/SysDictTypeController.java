package cc.iotkit.system.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.excel.utils.ExcelUtil;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.system.dto.bo.SysDictTypeBo;
import cc.iotkit.system.dto.vo.SysDictTypeVo;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cc.iotkit.system.service.ISysDictTypeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {

    private final ISysDictTypeService dictTypeService;

    /**
     * 查询字典类型列表
     */
    @ApiOperation(value = "查询字典类型列表", notes = "查询字典类型列表")
    @SaCheckPermission("system:dict:list")
    @PostMapping("/list")
    public Paging<SysDictTypeVo> list(  PageRequest<SysDictTypeBo> query) {
        return dictTypeService.selectPageDictTypeList( query);
    }

    /**
     * 导出字典类型列表
     */
    @ApiOperation(value = "导出字典类型列表", notes = "导出字典类型列表")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(@RequestBody SysDictTypeBo dictType, HttpServletResponse response) {

        List<SysDictTypeVo> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil.exportExcel(list, "字典类型", SysDictTypeVo.class, response);
    }

    /**
     * 查询字典类型详细
     *

     */
    @ApiOperation(value = "查询字典类型详细", notes = "查询字典类型详细")
    @SaCheckPermission("system:dict:query")
    @PostMapping(value = "/getById")
    public SysDictTypeVo getInfo(@Validated @RequestBody Request<Long> bo) {
        return dictTypeService.selectDictTypeById(bo.getData());
    }

    /**
     * 新增字典类型
     */
    @ApiOperation(value = "新增字典类型", notes = "新增字典类型")
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysDictTypeBo dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.insertDictType(dict);
    }

    /**
     * 修改字典类型
     */
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public void edit(@Validated @RequestBody SysDictTypeBo dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.updateDictType(dict);
    }

    /**
     * 删除字典类型
     *
     * @param dictIds 字典ID串
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public void remove(@PathVariable Collection<Long> dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
    }

    /**
     * 刷新字典缓存
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public void refreshCache() {
        dictTypeService.resetDictCache();
    }

    /**
     * 获取字典选择框列表
     */
    @PostMapping("/optionselect")
    public List<SysDictTypeVo> optionselect() {
        return dictTypeService.selectDictTypeAll();
    }
}
