package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.mqtt.MqttComponent;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.comps.config.ComponentConfig;
import cc.iotkit.converter.ScriptConverter;
import cc.iotkit.dao.ProtocolComponentRepository;
import cc.iotkit.dao.ProtocolConverterRepository;
import cc.iotkit.dao.UserInfoRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.Paging;
import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.protocol.ProtocolConverter;
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
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Autowired
    private ComponentConfig componentConfig;

    @Autowired
    private ProtocolComponentRepository protocolComponentRepository;

    @Autowired
    private ProtocolConverterRepository protocolConverterRepository;

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ComponentManager componentManager;

    @PostMapping("/uploadJar")
    public String uploadJar(@RequestParam("file") MultipartFile file, String id) {
        if (file == null) {
            throw new BizException("file is null");
        }
        log.info("saving upload jar file:{}", file.getName());
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (StringUtils.hasLength(id)) {
                getAndCheckComponent(id);
            } else {
                id = UUID.randomUUID().toString();
            }
            Path jarFilePath = componentConfig.getComponentFilePath(id);
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
        Path jarPath = componentConfig.getComponentFilePath(id);
        if (!jarPath.resolve(component.getJarFile()).toFile().exists()) {
            throw new BizException("component jar file does not exist");
        }

        Optional<ProtocolComponent> optComponent = protocolComponentRepository.findById(id);
        if (optComponent.isPresent()) {
            throw new BizException("component already exists");
        }
        try {
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
        Path jarPath = componentConfig.getComponentFilePath(id);
        if (!jarPath.resolve(component.getJarFile()).toFile().exists()) {
            throw new BizException("component jar file does not exist");
        }

        ProtocolComponent oldComponent = getAndCheckComponent(id);
        component = ReflectUtil.copyNoNulls(component, oldComponent);
        try {
            componentManager.deRegister(id);
            protocolComponentRepository.save(component);
        } catch (Throwable e) {
            throw new BizException("add protocol component error", e);
        }
    }

    @GetMapping("/getComponentScript/{id}")
    public String getComponentScript(@PathVariable("id") String id) {
        getAndCheckComponent(id);
        try {
            Path path = componentConfig.getComponentFilePath(id);
            File file = path.resolve(ProtocolComponent.SCRIPT_FILE_NAME).toFile();
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (Throwable e) {
            log.error("read component script file error", e);
            return "";
        }
    }

    @PostMapping("/saveComponentScript/{id}")
    public void saveComponentScript(
            @PathVariable("id") String id,
            @RequestBody String script) {
        ProtocolComponent oldComponent = getAndCheckComponent(id);
        try {
            Path path = componentConfig.getComponentFilePath(id);
            File file = path.resolve(ProtocolComponent.SCRIPT_FILE_NAME).toFile();
            script = JsonUtil.parse(script, String.class);
            FileUtils.writeStringToFile(file, script, "UTF-8", false);

            componentManager.deRegister(id);
            protocolComponentRepository.save(oldComponent);
        } catch (Throwable e) {
            throw new BizException("save protocol component script error", e);
        }
    }

    private ProtocolComponent getAndCheckComponent(@PathVariable("id") String id) {
        Optional<ProtocolComponent> optComponent = protocolComponentRepository.findById(id);
        if (!optComponent.isPresent()) {
            throw new BizException("the component does not exists");
        }
        ProtocolComponent oldComponent = optComponent.get();
        dataOwnerService.checkOwner(oldComponent);
        return oldComponent;
    }

    @PostMapping("/deleteComponent/{id}")
    public void deleteComponent(@PathVariable("id") String id) {
        ProtocolComponent component = getAndCheckComponent(id);
        try {
            componentManager.deRegister(id);

            Path path = Paths.get(String.format("%s/%s", componentConfig.getComponentDir(), id))
                    .toAbsolutePath().normalize();
            File file = path.toFile();
            try {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    FileUtils.delete(file);
                }
            } catch (NoSuchFileException e) {
                log.warn("delete component script error", e);
            }
            protocolComponentRepository.deleteById(component.getId());
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
        components.getContent().forEach(c -> c.setState(componentManager.isRunning(c.getId()) ?
                ProtocolComponent.STATE_RUNNING : ProtocolComponent.STATE_STOPPED));
        return new Paging<>(components.getTotalElements(), components.getContent());
    }

    @PostMapping("/converters/{size}/{page}")
    public Paging<ProtocolConverter> getConverters(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        protocolConverterRepository.deleteById("");
        protocolConverterRepository.deleteById("null");
        Page<ProtocolConverter> converters = protocolConverterRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createAt"))));
        return new Paging<>(converters.getTotalElements(), converters.getContent());
    }

    @PostMapping("/addConverter")
    public void addConverter(ProtocolConverter converter) {
        try {
            converter.setId(null);
            converter.setCreateAt(System.currentTimeMillis());
            converter.setUid(AuthUtil.getUserId());
            protocolConverterRepository.save(converter);
        } catch (Throwable e) {
            throw new BizException("add protocol converter error", e);
        }
    }

    @PostMapping("/saveConverter")
    public void saveConverter(ProtocolConverter converter) {
        ProtocolConverter oldConverter = getAndCheckConverter(converter.getId());
        converter = ReflectUtil.copyNoNulls(converter, oldConverter);
        try {
            protocolConverterRepository.save(converter);
        } catch (Throwable e) {
            throw new BizException("add protocol converter error", e);
        }
    }

    private ProtocolConverter getAndCheckConverter(String id) {
        Optional<ProtocolConverter> optConverter = protocolConverterRepository.findById(id);
        if (!optConverter.isPresent()) {
            throw new BizException("the protocol converter does not exists");
        }

        ProtocolConverter converter = optConverter.get();
        dataOwnerService.checkOwner(converter);
        return converter;
    }

    @GetMapping("/getConverterScript/{id}")
    public String getConverterScript(@PathVariable("id") String id) {
        getAndCheckConverter(id);
        try {
            Path path = componentConfig.getConverterFilePath(id);
            File file = path.resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile();
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (Throwable e) {
            log.error("read converter script file error", e);
            return "";
        }
    }

    @PostMapping("/saveConverterScript/{id}")
    public void saveConverterScript(
            @PathVariable("id") String id,
            @RequestBody String script) {
        getAndCheckConverter(id);
        try {
            Path path = componentConfig.getConverterFilePath(id);
            File file = path.resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile();
            script = JsonUtil.parse(script, String.class);
            FileUtils.writeStringToFile(file, script, "UTF-8", false);
        } catch (Throwable e) {
            throw new BizException("save protocol converter script error", e);
        }
    }

    @PostMapping("/deleteConverter/{id}")
    public void deleteConverter(@PathVariable("id") String id) {
        getAndCheckConverter(id);
        try {
            Path path = Paths.get(String.format("%s/%s", componentConfig.getConverterDir(), id))
                    .toAbsolutePath().normalize();
            File file = path.toFile();
            try {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    FileUtils.delete(file);
                }
            } catch (NoSuchFileException e) {
                log.warn("delete converter script error", e);
            }
            protocolConverterRepository.deleteById(id);
        } catch (Throwable e) {
            throw new BizException("delete protocol converter error", e);
        }
    }

    @PostMapping("/component/{id}/state/{state}")
    public void changeComponentState(@PathVariable("id") String id,
                                     @PathVariable("state") String state) {
        ProtocolComponent component = getAndCheckComponent(id);
        String converterId = component.getConverter();
        getAndCheckConverter(converterId);
        if (ProtocolComponent.STATE_RUNNING.equals(state)) {
            componentManager.register(component);
            componentManager.start(component.getId());
            component.setState(ProtocolComponent.STATE_RUNNING);
        } else {
            componentManager.deRegister(id);
            component.setState(ProtocolComponent.STATE_STOPPED);
        }
        protocolComponentRepository.save(component);
    }

}
