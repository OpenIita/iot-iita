package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.manager.dto.bo.channel.ChannelConfigBo;
import cc.iotkit.manager.dto.vo.channel.ChannelConfigVo;
import cc.iotkit.manager.service.NotifyService;
import cc.iotkit.model.notify.Channel;
import cc.iotkit.model.notify.ChannelConfig;
import cc.iotkit.model.notify.ChannelTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    public List<Channel> getChannelList() {
        return notifyService.getChannelList();
    }

    @ApiOperation("获取通道配置列表")
    @PostMapping("/channel/config/getList")
    public Paging<ChannelConfigVo> getChannelConfigList(PageRequest<ChannelConfigBo> request) {
        return notifyService.getChannelConfigList(request);
    }

    @ApiOperation("新增通道配置")
    @PostMapping("/channel/config/add")
    public ChannelConfig addChannelConfig(@RequestBody @Validated Request<ChannelConfig> request) {
        return notifyService.addChannelConfig(request.getData());
    }

    @ApiOperation("根据ID获取通道配置")
    @PostMapping("/channel/config/getById")
    public ChannelConfig getChannelConfigById(@RequestBody @Validated Request<String> request) {
        return notifyService.getChannelConfigById(request.getData());
    }

    @ApiOperation("修改通道配置")
    @PostMapping("/channel/config/updateById")
    public ChannelConfig updateChannelConfigById(@RequestBody @Validated Request<ChannelConfig> request) {
        return notifyService.updateChannelConfigById(request.getData());
    }

    @ApiOperation("删除通道配置")
    @PostMapping("/channel/config/delById")
    public Boolean delChannelConfigById(@RequestBody @Validated Request<String> request) {
        return notifyService.delChannelConfigById(request.getData());
    }

    @ApiOperation("获取通道模板列表")
    @PostMapping("/channel/template/getList")
    public List<ChannelTemplate> getChannelTemplateList() {
        return notifyService.getChannelTemplateList();
    }

    @ApiOperation("新增通道模板")
    @PostMapping("/channel/template/add")
    public ChannelTemplate addChannelTemplate(@RequestBody @Validated Request<ChannelTemplate> request) {
        return notifyService.addChannelTemplate(request.getData());
    }

    @ApiOperation("根据ID获取通道模板")
    @PostMapping("/channel/template/getById")
    public ChannelTemplate getChannelTemplateById(@RequestBody @Validated Request<String> request) {
        return notifyService.getChannelTemplateById(request.getData());
    }

    @ApiOperation("修改通道模板")
    @PostMapping("/channel/template/updateById")
    public ChannelTemplate updateChannelTemplateById(@RequestBody @Validated Request<ChannelTemplate> request) {
        return notifyService.updateChannelTemplateById(request.getData());
    }

    @ApiOperation("删除通道模板")
    @PostMapping("/channel/template/delById")
    public Boolean delChannelTemplateById(@RequestBody @Validated Request<String> request) {
        return notifyService.delChannelTemplateById(request.getData());
    }

}
