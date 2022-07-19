/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.comps.config.ComponentConfig;
import cc.iotkit.data.IProtocolComponentData;
import cc.iotkit.data.IProtocolConverterData;
import cc.iotkit.data.IUserInfoData;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.protocol.ProtocolConverter;
import cc.iotkit.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Autowired
    private ComponentConfig componentConfig;

    @Autowired
    private IProtocolComponentData protocolComponentData;

    @Autowired
    private IProtocolConverterData protocolConverterData;

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private IUserInfoData userInfoData;

    @Autowired
    private ComponentManager componentManager;

    @PostMapping("/uploadJar")
    public String uploadJar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("id") String id) {
        if (file == null) {
            throw new BizException("file is null");
        }
        log.info("saving upload jar file:{}", file.getName());
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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

        ProtocolComponent protocolComponent = protocolComponentData.findById(id);
        if (protocolComponent == null) {
            throw new BizException("component already exists");
        }
        try {
            component.setCreateAt(System.currentTimeMillis());
            component.setUid(AuthUtil.getUserId());
            protocolComponentData.save(component);
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
            protocolComponentData.save(component);
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
        getAndCheckComponent(id);
        try {
            Path path = componentConfig.getComponentFilePath(id);
            File file = path.resolve(ProtocolComponent.SCRIPT_FILE_NAME).toFile();
            script = JsonUtil.parse(script, String.class);
            FileUtils.writeStringToFile(file, script, "UTF-8", false);

            componentManager.deRegister(id);
        } catch (Throwable e) {
            throw new BizException("save protocol component script error", e);
        }
    }

    private ProtocolComponent getAndCheckComponent(@PathVariable("id") String id) {
        ProtocolComponent oldComponent = protocolComponentData.findById(id);
        if (oldComponent == null) {
            throw new BizException("the component does not exists");
        }
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
            protocolComponentData.deleteById(component.getId());
        } catch (Throwable e) {
            throw new BizException("delete protocol component error", e);
        }
    }

    @PostMapping("/components/{size}/{page}")
    public Paging<ProtocolComponent> getComponents(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        Paging<ProtocolComponent> components = protocolComponentData.findAll(page, size);
        components.getData().forEach(c -> c.setState(
                componentManager.isRunning(c.getId()) ?
                        ProtocolComponent.STATE_RUNNING : ProtocolComponent.STATE_STOPPED
        ));
        return components;
    }

    @PostMapping("/converters/{size}/{page}")
    public Paging<ProtocolConverter> getConverters(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        return protocolConverterData.findAll(page, size);
    }

    @PostMapping("/addConverter")
    public void addConverter(ProtocolConverter converter) {
        try {
            converter.setId(null);
            converter.setCreateAt(System.currentTimeMillis());
            converter.setUid(AuthUtil.getUserId());
            protocolConverterData.save(converter);
        } catch (Throwable e) {
            throw new BizException("add protocol converter error", e);
        }
    }

    @PostMapping("/saveConverter")
    public void saveConverter(ProtocolConverter converter) {
        ProtocolConverter oldConverter = getAndCheckConverter(converter.getId());
        converter = ReflectUtil.copyNoNulls(converter, oldConverter);
        try {
            protocolConverterData.save(converter);
        } catch (Throwable e) {
            throw new BizException("add protocol converter error", e);
        }
    }

    private ProtocolConverter getAndCheckConverter(String id) {
        ProtocolConverter converter = protocolConverterData.findById(id);
        if (converter == null) {
            throw new BizException("the protocol converter does not exists");
        }

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
            protocolConverterData.deleteById(id);
        } catch (Throwable e) {
            throw new BizException("delete protocol converter error", e);
        }
    }

    @PostMapping("/component/{id}/state/{state}")
    public void changeComponentState(@PathVariable("id") String id,
                                     @PathVariable("state") String state) {
        ProtocolComponent component = getAndCheckComponent(id);
        if (ProtocolComponent.TYPE_DEVICE.equals(component.getType())) {
            String converterId = component.getConverter();
            getAndCheckConverter(converterId);
        }

        if (ProtocolComponent.STATE_RUNNING.equals(state)) {
            componentManager.register(component);
            componentManager.start(component.getId());
            component.setState(ProtocolComponent.STATE_RUNNING);
        } else {
            componentManager.deRegister(id);
            component.setState(ProtocolComponent.STATE_STOPPED);
        }
        protocolComponentData.save(component);
    }

}
