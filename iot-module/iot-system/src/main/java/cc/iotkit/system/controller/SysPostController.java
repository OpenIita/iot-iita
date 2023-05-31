package cc.iotkit.system.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.excel.utils.ExcelUtil;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.system.dto.bo.SysPostBo;
import cc.iotkit.system.dto.vo.SysPostVo;
import cc.iotkit.system.service.ISysPostService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/post")
@Api(tags = "岗位信息")
public class SysPostController extends BaseController {

    private final ISysPostService postService;

    /**
     * 获取岗位列表
     */
    @ApiOperation("获取岗位列表")
    @SaCheckPermission("system:post:list")
    @PostMapping("/list")
    public Paging<SysPostVo> list(SysPostBo post, PageRequest<?> query) {
        return postService.selectPagePostList(post, query);
    }

    /**
     * 导出岗位列表
     */
    @ApiOperation("导出岗位列表")
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:post:export")
    @PostMapping("/export")
    public void export(SysPostBo post, HttpServletResponse response) {
        List<SysPostVo> list = postService.selectPostList(post);
        ExcelUtil.exportExcel(list, "岗位数据", SysPostVo.class, response);
    }

    /**
     * 根据岗位编号获取详细信息
     *
     * @param postId 岗位ID
     */
    @ApiOperation("根据岗位编号获取详细信息")
    @SaCheckPermission("system:post:query")
    @PostMapping(value = "/{postId}")
    public SysPostVo getInfo(@PathVariable Long postId) {
        return postService.selectPostById(postId);
    }

    /**
     * 新增岗位
     */
    @ApiOperation("新增岗位")
    @SaCheckPermission("system:post:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysPostBo post) {
        if (!postService.checkPostNameUnique(post)) {
            fail("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            fail("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        postService.insertPost(post);
    }

    /**
     * 修改岗位
     */
    @ApiOperation("修改岗位")
    @SaCheckPermission("system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PostMapping
    public void edit(@Validated @RequestBody SysPostBo post) {
        if (!postService.checkPostNameUnique(post)) {
            fail("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            fail("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        postService.updatePost(post);
    }

    /**
     * 删除岗位
     *
     * @param postIds 岗位ID串
     */
    @ApiOperation("删除岗位")
    @SaCheckPermission("system:post:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/{postIds}")
    public void remove(@PathVariable Collection postIds) {
        postService.deletePostByIds(postIds);
    }

    /**
     * 获取岗位选择框列表
     */
    @ApiOperation("获取岗位选择框列表")
    @PostMapping("/optionselect")
    public List<SysPostVo> optionselect() {
        return postService.selectPostAll();
    }
}
