package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.DeviceCache;
import cc.iotkit.dao.ProductCache;
import cc.iotkit.dao.UserActionLogRepository;
import cc.iotkit.manager.model.vo.MessageVo;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.device.message.DeviceEvent;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.model.UserActionLog;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController("api-msg")
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private UserActionLogRepository userActionLogRepository;
    @Autowired
    private ProductCache productCache;
    @Autowired
    private DeviceCache deviceCache;

    @ApiOperation("取系统消息")
    @PostMapping("/getSysMessages")
    public List<MessageVo> getSysMessages() {
        return new ArrayList<>();
    }

    @ApiOperation("取设备消息")
    @PostMapping("/getDeviceMessages")
    public List<MessageVo> getDeviceMessages() {
        List<UserActionLog> logs = userActionLogRepository.findAll(
                Example.of(UserActionLog.builder()
                        .uid(AuthUtil.getUserId())
                        .type(UserActionLog.Type.DEVICE_CONTROL.getValue())
                        .build()),
                PageRequest.of(1, 20, Sort.by(Sort.Order.desc("createAt")))
        ).getContent();

        List<MessageVo> messages = new ArrayList<>();
        logs.forEach(log -> messages.add(
                new MessageVo(
                        getDeviceMsg(log.getTarget(), log.getLog()),
                        new DateTime(log.getCreateAt()).toString("MM-dd HH:mm")
                )
                )
        );
        return messages;
    }

    private String getDeviceMsg(String target, Object log) {
        StringBuffer logMsg = new StringBuffer();
        if (log instanceof DeviceEvent) {
            DeviceEvent de = (DeviceEvent) log;
            DeviceInfo device = deviceCache.findByDeviceId(de.getDeviceId());
            ThingModel thingModel = productCache.getThingModel(device.getProductKey());
            if (thingModel == null) {
                return logMsg.toString();
            }
            ThingModel.Model model = thingModel.getModel();
            logMsg.append("将【").append(target).append("】");

            String identifier = de.getIdentifier();
            if (de.getType().equals("property")) {
                Object params = de.getRequest().getParams();
                if (params instanceof Map) {
                    ((Map<?, ?>) params).forEach((key, value) -> {
                        Optional<ThingModel.Property> property =
                                model.getProperties().stream()
                                        .filter((p) -> p.getIdentifier().equals(key))
                                        .findAny();
                        property.ifPresent(prop ->
                                logMsg.append(String.format("设置【%s】为%s", prop.getName(), value)));
                    });
                }
            } else {
                model.getServices().forEach(service -> {
                    if (service.getIdentifier().equals(identifier)) {
                        logMsg.append(service.getName());
                    }
                });
            }
        }
        return logMsg.toString();
    }

}
