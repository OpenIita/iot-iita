package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Request;
import cc.iotkit.manager.service.NotifyService;
import cc.iotkit.message.enums.MessageTypeEnum;
import cc.iotkit.message.model.Message;
import cc.iotkit.message.service.MessageService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.Channel;
import cc.iotkit.model.notify.ChannelConfig;
import cc.iotkit.model.notify.ChannelTemplate;
import cc.iotkit.model.notify.NotifyMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private MessageService messageService;

    @ApiOperation("获取通道类型列表")
    @PostMapping("/channel/getList")
    public List<Channel> getChannelList() {
        return notifyService.getChannelList();
    }

    @ApiOperation("获取通道配置列表")
    @PostMapping("/channel/config/getList")
    public List<ChannelConfig> getChannelConfigList() {
        return notifyService.getChannelConfigList();
    }

    @ApiOperation("新增通道配置")
    @PostMapping("/channel/config/add")
    public ChannelConfig addChannelConfig(@RequestBody @Valid Request<ChannelConfig> request) {
        return notifyService.addChannelConfig(request.getData());
    }

    @ApiOperation("根据ID获取通道配置")
    @PostMapping("/channel/config/getById")
    public ChannelConfig getChannelConfigById(@RequestBody @Valid Request<String> request) {
        return notifyService.getChannelConfigById(request.getData());
    }

    @ApiOperation("修改通道配置")
    @PostMapping("/channel/config/updateById")
    public ChannelConfig updateChannelConfigById(@RequestBody @Valid Request<ChannelConfig> request) {
        return notifyService.updateChannelConfigById(request.getData());
    }

    @ApiOperation("删除通道配置")
    @PostMapping("/channel/config/delById")
    public Boolean delChannelConfigById(@RequestBody @Valid Request<String> request) {
        return notifyService.delChannelConfigById(request.getData());
    }

    @ApiOperation("获取通道模板列表")
    @PostMapping("/channel/template/getList")
    public List<ChannelTemplate> getChannelTemplateList() {
        return notifyService.getChannelTemplateList();
    }

    @ApiOperation("新增通道模板")
    @PostMapping("/channel/template/add")
    public ChannelTemplate addChannelTemplate(@RequestBody @Valid Request<ChannelTemplate> request) {
        return notifyService.addChannelTemplate(request.getData());
    }

    @ApiOperation("根据ID获取通道模板")
    @PostMapping("/channel/template/getById")
    public ChannelTemplate getChannelTemplateById(@RequestBody @Valid Request<String> request) {
        return notifyService.getChannelTemplateById(request.getData());
    }

    @ApiOperation("修改通道模板")
    @PostMapping("/channel/template/updateById")
    public ChannelTemplate updateChannelTemplateById(@RequestBody @Valid Request<ChannelTemplate> request) {
        return notifyService.updateChannelTemplateById(request.getData());
    }

    @ApiOperation("删除通道模板")
    @PostMapping("/channel/template/delById")
    public Boolean delChannelTemplateById(@RequestBody @Valid Request<String> request) {
        return notifyService.delChannelTemplateById(request.getData());
    }

    @ApiOperation("消息列表")
    @PostMapping("/message/getList")
    public Paging<NotifyMessage> messageList(@RequestBody @Valid PageRequest<Void> request) {
        return notifyService.getNotifyMessageList(request.getPageNo(), request.getPageSize());
    }

    @ApiOperation("测试告警信息发送")
    @PostMapping("/message/testNotify")
    public void testNotify() {
        Map<String, String> param = new HashMap<>();
        param.put("title", "电磁炉");
        messageService.sendMessage(Message.builder().messageType(MessageTypeEnum.ALERT).param(param).channelTemplateId("7fd90ab1-63c8-cd92-2a58-b7bd04f557f1").build());
    }

}
