package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.mqtt.MqttComponent;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.converter.ScriptConverter;
import cc.iotkit.dao.ProtocolComponentRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.Paging;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.protocol.ProtocolComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Value("${gateway.function-jar}")
    private String functionJar;

    @Value("${component.dir}")
    private String componentDir;

    @Autowired
    private ProtocolComponentRepository protocolComponentRepository;

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ComponentManager componentManager;

    private Path getFilePath(String comId, String type) {
        return Paths.get(String.format("%s/%s/%s", componentDir, comId, type))
                .toAbsolutePath().normalize();
    }

    @PostMapping("/uploadJar")
    public String uploadJar(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            throw new BizException("file is null");
        }
        log.info("saving upload jar file:{}", file.getName());
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            String id = UUID.randomUUID().toString();
            Path jarFilePath = getFilePath(id, "jar");
            Files.createDirectories(jarFilePath);
            Path targetLocation = jarFilePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return id;
        } catch (IOException ex) {
            throw new BizException("upload jar error", ex);
        }
    }

    @PostMapping("/addComponent")
    public void addComponent(ProtocolComponent component) {
        String id = component.getId();
        if (!StringUtils.hasLength(id)) {
            throw new BizException("component id is blank");
        }
        Path jarPath = getFilePath(id, "jar");
        if (!jarPath.resolve(component.getJarFile()).toFile().exists()) {
            throw new BizException("component jar file does not exist");
        }

        Optional<ProtocolComponent> optComponent = protocolComponentRepository.findById(id);
        if (optComponent.isPresent()) {
            throw new BizException("component already exists");
        }
        try {
            Optional<UserInfo> optUser = userInfoRepository.findById(AuthUtil.getUserId());
            if (!optUser.isPresent()) {
                throw new BizException("user does not exists");
            }

            component.setCreateAt(System.currentTimeMillis());
            component.setUid(AuthUtil.getUserId());
            protocolComponentRepository.save(component);
        } catch (Throwable e) {
            throw new BizException("add protocol component error", e);
        }
    }

    @PostMapping("/saveComponent")
    public void saveComponent(ProtocolComponent component) {
        String id = component.getId();
        if (!StringUtils.hasLength(id)) {
            throw new BizException("component id is blank");
        }
        Path jarPath = getFilePath(id, "jar");
        if (!jarPath.resolve(component.getJarFile()).toFile().exists()) {
            throw new BizException("component jar file does not exist");
        }

        Optional<ProtocolComponent> optComponent = protocolComponentRepository.findById(component.getId());
        if (!optComponent.isPresent()) {
            throw new BizException("the protocol component does not exists");
        }
        Optional<UserInfo> optUser = userInfoRepository.findById(AuthUtil.getUserId());
        if (!optUser.isPresent()) {
            throw new BizException("user does not exists");
        }

        ProtocolComponent oldComponent = optComponent.get();
        component = ReflectUtil.copyNoNulls(component, oldComponent);
        dataOwnerService.checkOwner(component);
        try {
            protocolComponentRepository.save(component);
        } catch (Throwable e) {
            throw new BizException("add protocol component error", e);
        }
    }

    @PostMapping("/saveComponentScript")
    public void saveComponentScript(@RequestBody ProtocolComponent component) {
        Optional<ProtocolComponent> optComponent = protocolComponentRepository.findById(component.getId());
        if (!optComponent.isPresent()) {
            throw new BizException("the component does not exists");
        }
        dataOwnerService.checkOwner(component);
        ProtocolComponent oldComponent = optComponent.get();
        oldComponent.setScriptFile(component.getScriptFile());
        try {
//            gatewayService.saveFunction(oldGateway.getUuid(), oldGateway.getId(),
//                    "new (function (){" + oldGateway.getScript() + "})()", functionJar);
            protocolComponentRepository.save(oldComponent);
        } catch (Throwable e) {
            throw new BizException("save protocol component script error", e);
        }
    }

    @PostMapping("/deleteComponent/{id}")
    public void deleteComponent(@PathVariable("id") String id) {
        dataOwnerService.checkOwner(protocolComponentRepository, id);
        try {
            Path path = Paths.get(String.format("%s/%s", componentDir, id))
                    .toAbsolutePath().normalize();
            File file = path.toFile();
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                FileUtils.delete(file);
            }
            protocolComponentRepository.deleteById(id);
        } catch (Throwable e) {
            throw new BizException("delete protocol component error", e);
        }
    }

    @PostMapping("/components/{size}/{page}")
    public Paging<ProtocolComponent> getComponents(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        Page<ProtocolComponent> components = protocolComponentRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createAt"))));
        return new Paging<>(components.getTotalElements(), components.getContent());
    }

    @GetMapping("/registerMqtt")
    public void registerMqtt() throws IOException {
        MqttComponent component = new MqttComponent();
        component.create(new CompConfig(300, "{\"port\":2883,\"ssl\":false}"));
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
