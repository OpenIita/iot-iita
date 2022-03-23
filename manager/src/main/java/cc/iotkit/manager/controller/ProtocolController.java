package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.comp.mqtt.MqttComponent;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.converter.ScriptConverter;
import cc.iotkit.dao.ProtocolGatewayRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.Paging;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.protocol.ProtocolGateway;
import cc.iotkit.protocol.server.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Value("${gateway.function-jar}")
    private String functionJar;

    @Autowired
    private ProtocolGatewayRepository gatewayRepository;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ComponentManager componentManager;

    @PostMapping("/addGateway")
    public void addGateway(ProtocolGateway gateway) {
        Optional<ProtocolGateway> optGateway = gatewayRepository.findById(gateway.getId());
        if (optGateway.isPresent()) {
            throw new BizException("gateway already exists");
        }
        try {
            Optional<UserInfo> optUser = userInfoRepository.findById(AuthUtil.getUserId());
            if (!optUser.isPresent()) {
                throw new BizException("user does not exists");
            }

            gateway.setScript("new (function () {this.decode = function (msg) {return null; };})().decode(msg)");
            gateway.setCreateAt(System.currentTimeMillis());
            gateway.setUid(AuthUtil.getUserId());
            gateway.setUuid(optUser.get().getUid());
            gatewayService.saveFunction(gateway.getUuid(), gateway.getId(), gateway.getScript(), functionJar);
            gatewayRepository.save(gateway);
        } catch (Throwable e) {
            throw new BizException("add protocol gateway error", e);
        }
    }

    @PostMapping("/saveGateway")
    public void saveGateway(ProtocolGateway gateway) {
        Optional<ProtocolGateway> optGateway = gatewayRepository.findById(gateway.getId());
        if (!optGateway.isPresent()) {
            throw new BizException("the gateway does not exists");
        }
        Optional<UserInfo> optUser = userInfoRepository.findById(AuthUtil.getUserId());
        if (!optUser.isPresent()) {
            throw new BizException("user does not exists");
        }

        ProtocolGateway oldGateway = optGateway.get();
        gateway = ReflectUtil.copyNoNulls(gateway, oldGateway);
        dataOwnerService.checkOwner(gateway);
        try {
            gatewayRepository.save(gateway);
            gatewayService.saveFunction(gateway.getUuid(), gateway.getId(), gateway.getScript(), functionJar);
        } catch (Throwable e) {
            throw new BizException("add protocol gateway error", e);
        }
    }

    @PostMapping("/saveGatewayScript")
    public void saveGatewayScript(@RequestBody ProtocolGateway gateway) {
        Optional<ProtocolGateway> optGateway = gatewayRepository.findById(gateway.getId());
        if (!optGateway.isPresent()) {
            throw new BizException("the gateway does not exists");
        }
        dataOwnerService.checkOwner(gateway);
        ProtocolGateway oldGateway = optGateway.get();
        oldGateway.setScript(gateway.getScript());
        try {
            gatewayService.saveFunction(oldGateway.getUuid(), oldGateway.getId(),
                    "new (function (){" + oldGateway.getScript() + "})()", functionJar);
            gatewayRepository.save(oldGateway);
        } catch (Throwable e) {
            throw new BizException("save protocol gateway script error", e);
        }
    }

    @PostMapping("/deleteGateway/{id}")
    public void deleteGateway(@PathVariable("id") String id) {
        dataOwnerService.checkOwner(gatewayRepository, id);
        try {
            gatewayRepository.deleteById(id);
            gatewayService.deleteFunction(AuthUtil.getUserId(), id);
        } catch (Throwable e) {
            throw new BizException("delete protocol gateway error", e);
        }
    }

    @PostMapping("/gateways/{size}/{page}")
    public Paging<ProtocolGateway> getGateways(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        Page<ProtocolGateway> gateways = gatewayRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createAt"))));
        return new Paging<>(gateways.getTotalElements(), gateways.getContent());
    }

    @GetMapping("/registerMqtt")
    public void registerMqtt() throws IOException {
        MqttComponent component = new MqttComponent();
        component.create("{\"port\":2883,\"ssl\":false}");
        ScriptConverter converter = new ScriptConverter();
        converter.setScript(FileUtils.readFileToString(new File("/Users/sjg/home/gitee/open-source/converter.js"), "UTF-8"));
        component.setConverter(converter);
        componentManager.register("123", component);
        componentManager.start("123", FileUtils.readFileToString(new File("/Users/sjg/home/gitee/open-source/component.js"), "UTF-8"));
    }

    @GetMapping("/deregisterMqtt")
    public void deregisterMqtt() {
        componentManager.stop("123");
        componentManager.deRegister("123");
    }
}
