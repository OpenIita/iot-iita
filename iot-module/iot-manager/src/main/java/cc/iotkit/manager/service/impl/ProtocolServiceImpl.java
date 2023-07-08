package cc.iotkit.manager.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.comps.config.ComponentConfig;
import cc.iotkit.data.manager.IProtocolComponentData;
import cc.iotkit.data.manager.IProtocolConverterData;
import cc.iotkit.data.manager.IUserInfoData;
import cc.iotkit.manager.dto.bo.ChangeStateBo;
import cc.iotkit.manager.dto.bo.protocolcomponent.ProtocolComponentBo;
import cc.iotkit.manager.dto.bo.protocolconverter.ProtocolConverterBo;
import cc.iotkit.manager.dto.vo.protocolcomponent.ProtocolComponentVo;
import cc.iotkit.manager.dto.vo.protocolconverter.ProtocolConverterVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.IProtocolService;
import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.protocol.ProtocolConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: jay
 * @Date: 2023/5/30 10:48
 * @Version: V1.0
 * @Description: 协议服务
 */

@Service
@Slf4j
public class ProtocolServiceImpl implements IProtocolService {

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
    @Override
    public String uploadJar(MultipartFile file, String id) {
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

    @Override
    public boolean addComponent(ProtocolComponentBo component) {
        String id = component.getId();
        //jar包上传后生成的id
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.COMPONENT_ID_BLANK);
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
            protocolComponentData.save(component.to(ProtocolComponent.class));
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_COMPONENT_ERROR, e);
        }
        return true;
    }

    @Override
    public String saveComponent(ProtocolComponentBo req) {
        ProtocolComponent component = req.to(ProtocolComponent.class);
        String id = component.getId();
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.COMPONENT_ID_BLANK);
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
            return component.getId();
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_COMPONENT_ERROR, e);
        }
    }

    @Override
    public ProtocolComponentVo getProtocolComponent(String id) {
        ProtocolComponent component = getAndCheckComponent(id);

        String script = component.getScript();
        // 如果数据库里不存在,则从文件中读取脚本
        if (!StringUtils.hasText(script)) {
            try {
                File file = getComponentScriptFile(id);
                script = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            } catch (Throwable e) {
                log.error("read converter script file error", e);
                script = "";
            }
            component.setScript(script);
        }
        return MapstructUtils.convert(component, ProtocolComponentVo.class);
    }

    @Override
    public boolean saveComponentScript(ProtocolComponentBo upReq) {
        String id = upReq.getId();
        ProtocolComponent old = getAndCheckComponent(id);
        try {
            // 保存到文件
            File file = getComponentScriptFile(id);
            String script = upReq.getScript();
            FileUtils.writeStringToFile(file, script, StandardCharsets.UTF_8, false);

            // 保存到数据库,后续加版本号
            old.setScript(upReq.getScript());
            old.setScriptTyp(upReq.getScriptTyp());
            protocolComponentData.save(old);

            componentManager.deRegister(id);
        } catch (Throwable e) {
            throw new BizException(ErrCode.SAVE_COMPONENT_SCRIPT_ERROR, e);
        }
        return true;
    }

    @Override
    public boolean deleteComponent(String id) {
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
        return true;
    }

    @Override
    public Paging<ProtocolComponentVo> selectPageList(PageRequest<ProtocolComponentBo> query) {
        Paging<ProtocolComponentVo> components = protocolComponentData.findAll(query.to(ProtocolComponent.class)).to(ProtocolComponentVo.class);
        components.getRows().forEach(c -> c.setState(
                componentManager.isRunning(c.getId()) ?
                        ProtocolComponent.STATE_RUNNING : ProtocolComponent.STATE_STOPPED
        ));
        return components;
    }

    @Override
    public Paging<ProtocolConverterVo> selectConvertersPageList(PageRequest<ProtocolConverterBo> query) {
       return protocolConverterData.findAll(query.to(ProtocolConverter.class)).to(ProtocolConverterVo.class);
    }

    @Override
    public boolean addConverter(ProtocolConverterBo req) {
        try {
            ProtocolConverter converter = req.to(ProtocolConverter.class);
            converter.setId(null);
            converter.setCreateAt(System.currentTimeMillis());
            converter.setUid(AuthUtil.getUserId());
            protocolConverterData.save(converter);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_CONVERT_ERROR, e);
        }
        return false;
    }

    @Override
    public boolean editConverter(ProtocolConverterBo req) {
        ProtocolConverter converter = req.to(ProtocolConverter.class);
        ProtocolConverter oldConverter = getAndCheckConverter(converter.getId());
        converter = ReflectUtil.copyNoNulls(converter, oldConverter);
        try {
            protocolConverterData.save(converter);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_CONVERT_ERROR, e);
        }
        return true;
    }

    @Override
    public ProtocolConverterVo getConverter(String id) {
        ProtocolConverter converter = getAndCheckConverter(id);
        String script = converter.getScript();
        // 如果数据库里不存在,则从文件中读取脚本
        if (!StringUtils.hasText(script)) {
            try {
                Path path = componentConfig.getConverterFilePath(id);
                File file = path.resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile();
                script = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            } catch (Throwable e) {
                log.error("read converter script file error", e);
                script = "";
            }
            converter.setScript(script);
        }
        return MapstructUtils.convert(converter, ProtocolConverterVo.class);
    }

    @Override
    public boolean saveConverterScript(ProtocolConverterBo req) {
        ProtocolConverter converter = req.to(ProtocolConverter.class);
        String id = req.getId();
        getAndCheckConverter(id);
        try {
            // 先存文件
            Path path = componentConfig.getConverterFilePath(id);
            File file = path.resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile();
            String script = converter.getScript();
            FileUtils.writeStringToFile(file, script, StandardCharsets.UTF_8, false);

            // 再存数据库
            protocolConverterData.save(converter);

        } catch (Throwable e) {
            throw new BizException(ErrCode.SAVE_CONVERT_SCRIPT_ERROR, e);
        }
        return true;
    }

    @Override
    public boolean deleteConverter(String id) {
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
        return true;
    }

    @Override
    public boolean changeComponentState(ChangeStateBo req) {
        String id = req.getId();
        String state = req.getState();
        ProtocolComponent component = getAndCheckComponent(id);
        if (ProtocolComponent.TYPE_DEVICE.equals(component.getType()) && ProtocolComponent.CONVER_TYPE_CUSTOM.equals(component.getConverType())) {
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
        return true;
    }


    /******************************/
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

    private ProtocolConverter getAndCheckConverter(String id) {
        ProtocolConverter converter = protocolConverterData.findById(id);
        if (converter == null) {
            throw new BizException(ErrCode.CONVERT_NOT_FOUND);
        }

        dataOwnerService.checkOwner(converter);
        return converter;
    }




}
