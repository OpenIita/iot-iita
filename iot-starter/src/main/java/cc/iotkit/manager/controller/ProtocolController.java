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

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
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
import io.swagger.annotations.Api;
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

@Api(tags = {"协议"})
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
            throw new BizException(ErrCode.PARAMS_EXCEPTION);
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
            throw new BizException(ErrCode.UPLOAD_FILE_ERROR, ex);
        }
    }

    @PostMapping("/addComponent")
    public void addComponent(ProtocolComponent component) {
        String id = component.getId();
        //jar包上传后生成的id
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        Path jarPath = componentConfig.getComponentFilePath(id);
        if (!jarPath.resolve(component.getJarFile()).toFile().exists()) {
            throw new BizException(ErrCode.COMPONENT_JAR_NOT_FOUND);
        }

        ProtocolComponent protocolComponent = protocolComponentData.findById(id);
        if (protocolComponent != null) {
            throw new BizException(ErrCode.COMPONENT_ALREADY);
        }
        try {
            component.setCreateAt(System.currentTimeMillis());
            component.setUid(AuthUtil.getUserId());
            protocolComponentData.save(component);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_COMPONENT_ERROR, e);
        }
    }

    @PostMapping("/saveComponent")
    public void saveComponent(ProtocolComponent component) {
        String id = component.getId();
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        Path jarPath = componentConfig.getComponentFilePath(id);
        if (!jarPath.resolve(component.getJarFile()).toFile().exists()) {
            throw new BizException(ErrCode.COMPONENT_JAR_NOT_FOUND);
        }

        ProtocolComponent oldComponent = getAndCheckComponent(id);
        component = ReflectUtil.copyNoNulls(component, oldComponent);

        try {
            componentManager.deRegister(id);
            protocolComponentData.save(component);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_COMPONENT_ERROR, e);
        }
    }

    @GetMapping("/getComponentScript/{id}")
    public ProtocolComponent getComponentScript(@PathVariable("id") String id) {
        ProtocolComponent component = getAndCheckComponent(id);

        String script = component.getScript();
        // 如果数据库里不存在,则从文件中读取脚本
        if(!StringUtils.hasText(script)){
            try {
                File file = getComponentScriptFile(id);
                script = FileUtils.readFileToString(file, "UTF-8");
            } catch (Throwable e) {
                log.error("read converter script file error", e);
                script = "";
            }
            component.setScript(script);
        }
        return component;

    }

    @PostMapping("/saveComponentScript/{id}")
    public void saveComponentScript(
            @PathVariable("id") String id,
            @RequestBody ProtocolComponent upReq) {
        ProtocolComponent old = getAndCheckComponent(id);
        try {
            // 保存到文件
            File file = getComponentScriptFile(id);
            String script = upReq.getScript();
            FileUtils.writeStringToFile(file, script, "UTF-8", false);

            // 保存到数据库,后续加版本号
            old.setScript(upReq.getScript());
            old.setScriptTyp(upReq.getScriptTyp());
            protocolComponentData.save(old);

            componentManager.deRegister(id);
        } catch (Throwable e) {
            throw new BizException(ErrCode.SAVE_COMPONENT_SCRIPT_ERROR, e);
        }
    }

    private File getComponentScriptFile(String id) {
        Path path = componentConfig.getComponentFilePath(id);
        return path.resolve(ProtocolComponent.SCRIPT_FILE_NAME).toFile();
    }

    private ProtocolComponent getAndCheckComponent(@PathVariable("id") String id) {
        ProtocolComponent oldComponent = protocolComponentData.findById(id);
        if (oldComponent == null) {
            throw new BizException(ErrCode.COMPONENT_NOT_FOUND);
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
            throw new BizException(ErrCode.DELETE_COMPONENT_ERROR, e);
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
            throw new BizException(ErrCode.ADD_CONVERT_ERROR, e);
        }
    }

    @PostMapping("/saveConverter")
    public void saveConverter(ProtocolConverter converter) {
        ProtocolConverter oldConverter = getAndCheckConverter(converter.getId());
        converter = ReflectUtil.copyNoNulls(converter, oldConverter);
        try {
            protocolConverterData.save(converter);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_CONVERT_ERROR, e);
        }
    }

    private ProtocolConverter getAndCheckConverter(String id) {
        ProtocolConverter converter = protocolConverterData.findById(id);
        if (converter == null) {
            throw new BizException(ErrCode.CONVERT_NOT_FOUND);
        }

        dataOwnerService.checkOwner(converter);
        return converter;
    }

    @GetMapping("/getConverterScript/{id}")
    public ProtocolConverter getConverterScript(@PathVariable("id") String id) {
        ProtocolConverter converter = getAndCheckConverter(id);
        String script = converter.getScript();
        // 如果数据库里不存在,则从文件中读取脚本
        if(!StringUtils.hasText(script)){
            try {
                Path path = componentConfig.getConverterFilePath(id);
                File file = path.resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile();
                script = FileUtils.readFileToString(file, "UTF-8");
            } catch (Throwable e) {
                log.error("read converter script file error", e);
                script = "";
            }
            converter.setScript(script);
        }
        return converter;

    }

    @PostMapping("/saveConverterScript/{id}")
    public void saveConverterScript(
            @PathVariable("id") String id,
            @RequestBody ProtocolConverter converter) {
        getAndCheckConverter(id);
        try {
            // 先存文件
            Path path = componentConfig.getConverterFilePath(id);
            File file = path.resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile();
            String script = converter.getScript();
            FileUtils.writeStringToFile(file, script, "UTF-8", false);

            // 再存数据库
            protocolConverterData.save(converter);

        } catch (Throwable e) {
            throw new BizException(ErrCode.SAVE_CONVERT_SCRIPT_ERROR, e);
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
            throw new BizException(ErrCode.DELETE_CONVERT_ERROR, e);
        }
    }

    @PostMapping("/component/{id}/state/{state}")
    public void changeComponentState(@PathVariable("id") String id,
                                     @PathVariable("state") String state) {
        ProtocolComponent component = getAndCheckComponent(id);
        if (ProtocolComponent.TYPE_DEVICE.equals(component.getType())&&ProtocolComponent.CONVER_TYPE_CUSTOM.equals(component.getConverType())) {
            String converterId = component.getConverter();
            getAndCheckConverter(converterId);
        }

        if (ProtocolComponent.STATE_RUNNING.equals(state)) {
            File scriptFile = getComponentScriptFile(id);
            if (!scriptFile.exists()) {
                throw new BizException("请先编写组件脚本");
            }

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
