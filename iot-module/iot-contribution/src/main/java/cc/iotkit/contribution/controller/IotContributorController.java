package cc.iotkit.contribution.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.excel.utils.ExcelUtil;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.contribution.dto.bo.IotContributorBo;
import cc.iotkit.contribution.dto.vo.IotContributorVo;
import cc.iotkit.contribution.service.IIotContributorService;
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
 * 贡献者
 *
 * @author Lion Li
 * @date 2023-07-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/contribution/contributor")
public class IotContributorController extends BaseController {

    private final IIotContributorService iotContributorService;

    /**
     * 查询贡献者列表
     */
    @SaCheckPermission("contribution:contributor:list")
    @PostMapping("/list")
    @ApiOperation("查询贡献者列表")
    public Paging<IotContributorVo> list( PageRequest<IotContributorBo> pageQuery) {
        return iotContributorService.queryPageList(pageQuery);
    }

    /**
     * 导出贡献者列表
     */
    @ApiOperation("导出贡献者列表")
    @SaCheckPermission("contribution:contributor:export")
    @Log(title = "贡献者", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(IotContributorBo bo, HttpServletResponse response) {
        List<IotContributorVo> list = iotContributorService.queryList(bo);
        ExcelUtil.exportExcel(list, "贡献者", IotContributorVo.class, response);
    }

    /**
     * 获取贡献者详细信息
     *
     */
    @SaCheckPermission("contribution:contributor:query")
    @PostMapping("/getDetail")
    @ApiOperation("获取贡献者详细信息")
    public IotContributorVo getDetail(@Validated @RequestBody Request<Long> request) {
        return iotContributorService.queryById(request.getData());
    }

    /**
     * 新增贡献者
     */
    @SaCheckPermission("contribution:contributor:add")
    @Log(title = "贡献者", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    @ApiOperation("新增贡献者")
    public Long add(@Validated(AddGroup.class) @RequestBody Request<IotContributorBo> request) {
        return iotContributorService.insertByBo(request.getData());
    }

    /**
     * 修改贡献者
     */
    @SaCheckPermission("contribution:contributor:edit")
    @Log(title = "贡献者", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ApiOperation("修改贡献者")
    public boolean edit(@Validated(EditGroup.class) @RequestBody  Request<IotContributorBo> request) {
        return iotContributorService.updateByBo(request.getData());
    }

    /**
     * 删除贡献者
     *
     */
    @SaCheckPermission("contribution:contributor:remove")
    @Log(title = "贡献者", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    @ApiOperation("删除贡献者")
    public boolean remove(@Validated @RequestBody Request<List<Long>> query) {
        return iotContributorService.deleteWithValidByIds(query.getData(), true);
    }
}
