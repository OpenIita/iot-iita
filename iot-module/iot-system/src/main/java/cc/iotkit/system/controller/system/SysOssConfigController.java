package cc.iotkit.system.controller.system;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.domain.vo.PagedDataVo;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.common.validate.QueryGroup;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.system.domain.bo.SysOssConfigBo;
import cc.iotkit.system.domain.vo.SysOssConfigVo;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cc.iotkit.system.service.ISysOssConfigService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 对象存储配置
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/resource/oss/config")
public class SysOssConfigController extends BaseController {

    private final ISysOssConfigService ossConfigService;

    /**
     * 查询对象存储配置列表
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public PagedDataVo<SysOssConfigVo> list(@Validated(QueryGroup.class) SysOssConfigBo bo, PageRequest<?> query) {
        return ossConfigService.queryPageList(bo, query);
    }

    /**
     * 获取对象存储配置详细信息
     *
     * @param ossConfigId OSS配置ID
     */
    @SaCheckPermission("system:oss:query")
    @GetMapping("/{ossConfigId}")
    public SysOssConfigVo getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long ossConfigId) {
        return ossConfigService.queryById(ossConfigId);
    }

    /**
     * 新增对象存储配置
     */
    @SaCheckPermission("system:oss:add")
    @Log(title = "对象存储配置", businessType = BusinessType.INSERT)
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody SysOssConfigBo bo) {
        ossConfigService.insertByBo(bo);
    }

    /**
     * 修改对象存储配置
     */
    @SaCheckPermission("system:oss:edit")
    @Log(title = "对象存储配置", businessType = BusinessType.UPDATE)
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody SysOssConfigBo bo) {
        ossConfigService.updateByBo(bo);
    }

    /**
     * 删除对象存储配置
     *
     * @param ossConfigIds OSS配置ID串
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "对象存储配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossConfigIds}")
    public void remove(@NotEmpty(message = "主键不能为空")
                       @PathVariable Long[] ossConfigIds) {
        ossConfigService.deleteWithValidByIds(List.of(ossConfigIds), true);
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:oss:edit")
    @Log(title = "对象存储状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public void changeStatus(@RequestBody SysOssConfigBo bo) {
        ossConfigService.updateOssConfigStatus(bo);
    }
}
