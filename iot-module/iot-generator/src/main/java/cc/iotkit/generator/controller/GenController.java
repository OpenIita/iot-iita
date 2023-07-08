package cc.iotkit.generator.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.generator.domain.GenTable;
import cc.iotkit.generator.domain.GenTableColumn;
import cc.iotkit.generator.dto.bo.ImportTableBo;
import cc.iotkit.generator.service.IGenTableService;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController {

    private final IGenTableService genTableService;

    /**
     * 查询代码生成列表
     */
//    @SaCheckPermission("tool:gen:list")
    @ApiOperation(value = "查询代码生成列表", notes = "查询代码生成列表,根据查询条件分页")
    @PostMapping("/list")
    public Paging<GenTable> genList(@RequestBody @Validated PageRequest<GenTable> query) {
        return genTableService.selectPageGenTableList(query );
    }

    /**
     * 修改代码生成业务
     *
     */
//    @SaCheckPermission("tool:gen:query")
    @ApiOperation(value = "修改代码生成业务", notes = "修改代码生成业务详情")
    @PostMapping(value = "/getDetail")
    public Map<String, Object> getInfo(@Validated @RequestBody Request<Long> bo) {
        Long tableId = bo.getData();
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return map;
    }

    /**
     * 查询数据库列表
     */
//    @SaCheckPermission("tool:gen:list")
    @ApiOperation(value = "查询数据库列表", notes = "查询数据库列表")
    @PostMapping("/db/list")
    public Paging<GenTable> dataList(@RequestBody @Validated PageRequest<GenTable> pageQuery) {
        return genTableService.selectPageDbTableList( pageQuery);
    }

    /**
     * 查询数据表字段列表
     *
     * @param tableId 表ID
     */
//    @SaCheckPermission("tool:gen:list")
    @ApiOperation(value = "查询数据表字段列表", notes = "查询数据表字段列表")
    @PostMapping(value = "/column/{tableId}")
    public Paging<GenTableColumn> columnList(Long tableId) {

        List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);

        return  new Paging<>();
    }

    /**
     * 导入表结构（保存）
     *
     */
//    @SaCheckPermission("tool:gen:import")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    @ApiOperation(value = "导入表结构（保存）", notes = "导入表结构（保存）")
    public void importTableSave(@Validated @RequestBody Request<ImportTableBo> bo) {
        List<String> tables = bo.getData().getTables();

        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tables);
        genTableService.importGenTable(tableList);
    }

    /**
     * 修改保存代码生成业务
     */
//    @SaCheckPermission("tool:gen:edit")
    @ApiOperation(value = "修改保存代码生成业务", notes = "修改保存代码生成业务")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public void editSave(@Validated @RequestBody Request<GenTable> bo) {
        GenTable genTable = bo.getData();
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
    }

    /**
     * 删除代码生成
     *

     */
//    @SaCheckPermission("tool:gen:remove")
    @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    @ApiOperation(value = "删除代码生成", notes = "删除代码生成")
    public void remove(@Validated @RequestBody Request<List<Long>> bo) {
        genTableService.deleteGenTableByIds(bo.getData());
    }

    /**
     * 预览代码
     *
     */
//    @SaCheckPermission("tool:gen:preview")
    @ApiOperation(value = "预览代码", notes = "预览代码")
    @PostMapping("/preview")
    public Map<String, String> preview(@Validated @RequestBody Request<Long> bo) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(bo.getData());
        return dataMap;
    }

    /**
     * 生成代码（下载方式）
     *
     * @param tableName 表名
     */
//    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @PostMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param tableName 表名
     */
//    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @ApiOperation(value = "生成代码（自定义路径）", notes = "生成代码（自定义路径）")
    @PostMapping("/genCode/{tableName}")
    public void genCode(@PathVariable("tableName") String tableName) {
        genTableService.generatorCode(tableName);
    }

    /**
     * 同步数据库
     *
     */
//    @SaCheckPermission("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "同步数据库", notes = "同步数据库")
    @PostMapping("/synchDb")
    public void synchDb(@Validated @RequestBody Request<String> bo) {
        genTableService.synchDb(bo.getData());
    }

    /**
     * 批量生成代码
     *
     * @param tables 表名串
     */
//    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @ApiOperation(value = "批量生成代码", notes = "批量生成代码")
    @PostMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }
}
