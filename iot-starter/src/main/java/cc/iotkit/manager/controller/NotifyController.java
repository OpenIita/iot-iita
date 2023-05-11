package cc.iotkit.manager.controller;

import cc.iotkit.manager.service.NotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * author: 石恒
 * date: 2023-05-11 15:17
 * description:
 **/
@Api(tags = {"消息通知"})
@Slf4j
@RestController
@RequestMapping("/notify")
public class NotifyController {

    @Resource
    private NotifyService notifyService;

    @ApiOperation("获取通道类型列表")
    @PostMapping("/channel/getList")
    public void getChannelList() {
    }

    @ApiOperation("获取通道配置列表")
    @PostMapping("/channel/config/getList")
    public void getChannelConfigList() {
    }

    @ApiOperation("新增通道配置")
    @PostMapping("/channel/config/add")
    public void addChannelConfig() {
    }

    @ApiOperation("根据ID获取通道配置")
    @PostMapping("/channel/config/getById")
    public void getChannelConfigById() {
    }

    @ApiOperation("修改通道配置")
    @PostMapping("/channel/config/updateById")
    public void updateChannelConfigById() {
    }

    @ApiOperation("删除通道配置")
    @PostMapping("/channel/config/delById")
    public void delChannelConfigById() {
    }

    @ApiOperation("获取通道模板列表")
    @PostMapping("/channel/template/getList")
    public void getChannelTemplateList() {
    }

    @ApiOperation("新增通道模板")
    @PostMapping("/channel/template/add")
    public void addChannelTemplate() {
    }

    @ApiOperation("根据ID获取通道模板")
    @PostMapping("/channel/template/getById")
    public void getChannelTemplateById() {
    }

    @ApiOperation("修改通道模板")
    @PostMapping("/channel/template/updateById")
    public void updateChannelTemplateById() {
    }

    @ApiOperation("删除通道模板")
    @PostMapping("/channel/template/delById")
    public void delChannelTemplateById() {
    }

}
