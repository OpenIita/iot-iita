/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.system.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.excel.utils.ExcelUtil;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.common.validate.QueryGroup;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.system.dto.bo.SysDictTypeBo;
import cc.iotkit.system.dto.vo.SysDictTypeVo;
import cc.iotkit.system.service.ISysDictTypeService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
    public Paging<SysDictTypeVo> list(@RequestBody @Validated(QueryGroup.class) PageRequest<SysDictTypeBo> query) {
        return dictTypeService.selectPageDictTypeList(query);
    }

    /**
     * 导出字典类型列表
     */
    @ApiOperation(value = "导出字典类型列表", notes = "导出字典类型列表")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(@Validated(QueryGroup.class) SysDictTypeBo dictType, HttpServletResponse response) {

        List<SysDictTypeVo> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil.exportExcel(list, "字典类型", SysDictTypeVo.class, response);
    }

    /**
     * 查询字典类型详细
     */
    @ApiOperation(value = "查询字典类型详细", notes = "查询字典类型详细")
    @SaCheckPermission("system:dict:query")
    @PostMapping(value = "/getById")
    public SysDictTypeVo getInfo(@Validated(QueryGroup.class) @RequestBody Request<SysDictTypeBo> bo) {
        Long dictTypeId = bo.getData().getId();
        return dictTypeService.selectDictTypeById(dictTypeId);
    }

    /**
     * 新增字典类型
     */
    @ApiOperation(value = "新增字典类型", notes = "新增字典类型")
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public void add(@Validated(AddGroup.class) @RequestBody Request<SysDictTypeBo> dict) {
        if (!dictTypeService.checkDictTypeUnique(dict.getData())) {
            fail("新增字典'" + dict.getData().getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.insertDictType(dict.getData());
    }

    /**
     * 修改字典类型
     */
    @ApiOperation(value = "修改字典类型", notes = "修改字典类型")
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public void edit(@Validated(EditGroup.class) @RequestBody Request<SysDictTypeBo> dict) {
        if (!dictTypeService.checkDictTypeUnique(dict.getData())) {
            fail("修改字典'" + dict.getData().getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.updateDictType(dict.getData());
    }

    /**
     * 删除字典类型
     *
     * @param dictIds 字典ID串
     */
    @ApiOperation(value = "删除字典类型", notes = "删除字典类型")
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public void remove(@RequestBody @Validated Request<List<Long>> dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds.getData());
    }

    /**
     * 刷新字典缓存
     */
    @ApiOperation(value = "刷新字典缓存", notes = "刷新字典缓存")
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @PostMapping("/refreshCache")
    public void refreshCache() {
        dictTypeService.resetDictCache();
    }

    /**
     * 获取字典选择框列表
     */
    @ApiOperation(value = "获取字典选择框列表", notes = "获取字典选择框列表")
    @PostMapping("/optionselect")
    public List<SysDictTypeVo> optionselect() {
        return dictTypeService.selectDictTypeAll();
    }
}
