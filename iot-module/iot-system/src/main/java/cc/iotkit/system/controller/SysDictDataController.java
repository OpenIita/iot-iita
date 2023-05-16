package cc.iotkit.system.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.domain.vo.PagedDataVo;
import cc.iotkit.common.excel.utils.ExcelUtil;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.system.domain.bo.SysDictDataBo;
import cc.iotkit.system.domain.vo.SysDictDataVo;
import cc.iotkit.system.service.ISysDictDataService;
import cc.iotkit.system.service.ISysDictTypeService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController {

    private final ISysDictDataService dictDataService;
    private final ISysDictTypeService dictTypeService;

    /**
     * 查询字典数据列表
     */
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public PagedDataVo<SysDictDataVo> list(SysDictDataBo dictData, PageRequest<?> query) {
        return dictDataService.selectPageDictDataList(dictData, query);
    }

    /**
     * 导出字典数据列表
     */
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(SysDictDataBo dictData, HttpServletResponse response) {
        List<SysDictDataVo> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil.exportExcel(list, "字典数据", SysDictDataVo.class, response);
    }

    /**
     * 查询字典数据详细
     *
     * @param dictCode 字典code
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public SysDictDataVo getInfo(@PathVariable Long dictCode) {
        return dictDataService.selectDictDataById(dictCode);
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     */
    @GetMapping(value = "/type/{dictType}")
    public List<SysDictDataVo> dictType(@PathVariable String dictType) {
        List<SysDictDataVo> data = dictTypeService.selectDictDataByType(dictType);
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<SysDictDataVo>();
        }
        return data;
    }

    /**
     * 新增字典类型
     */
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysDictDataBo dict) {
        dictDataService.insertDictData(dict);
    }

    /**
     * 修改保存字典类型
     */
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public void edit(@Validated @RequestBody SysDictDataBo dict) {
        dictDataService.updateDictData(dict);
    }

    /**
     * 删除字典类型
     *
     * @param dictCodes 字典code串
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public void remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
    }
}
