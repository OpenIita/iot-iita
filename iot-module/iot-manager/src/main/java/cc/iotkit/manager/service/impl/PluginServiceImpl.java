package cc.iotkit.manager.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.common.utils.file.FileUtils;
import cc.iotkit.data.manager.IPluginInfoData;
import cc.iotkit.manager.dto.bo.plugin.PluginInfoBo;
import cc.iotkit.manager.dto.vo.plugin.PluginInfoVo;
import cc.iotkit.manager.service.IPluginService;
import cc.iotkit.model.plugin.PluginInfo;
import cn.hutool.core.io.IoUtil;
import com.gitee.starblues.core.PluginState;
import com.gitee.starblues.core.descriptor.PluginDescriptor;
import com.gitee.starblues.integration.operator.PluginOperator;
import com.gitee.starblues.integration.operator.upload.UploadParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author sjg
 */
@Slf4j
@Service
public class PluginServiceImpl implements IPluginService {

    @Autowired
    private IPluginInfoData pluginInfoData;

    @Autowired
    private PluginOperator pluginOperator;

    @Override
    public void upload(MultipartFile file, Long id) {
        try {
            PluginInfo plugin = pluginInfoData.findById(id);
            if (plugin == null) {
                throw new BizException(ErrCode.DATA_NOT_EXIST);
            }
            String pluginId = plugin.getPluginId();

            if (StringUtils.isNotBlank(pluginId)) {
                //停止卸载旧的插件
                com.gitee.starblues.core.PluginInfo pluginInfo = pluginOperator.getPluginInfo(pluginId);
                if (pluginInfo != null) {
                    if (pluginInfo.getPluginState() == PluginState.STARTED) {
                        pluginOperator.stop(pluginId);
                    }
                    pluginOperator.uninstall(pluginId, true, false);
                    //兼容相同版本包，先删除，再上传
                    FileUtils.del(pluginInfo.getPluginDescriptor().getPluginPath());
                }
            }

            UploadParam uploadParam = UploadParam.byMultipartFile(file)
                    .setBackOldPlugin(false)
                    .setStartPlugin(false)
                    .setUnpackPlugin(false);
            com.gitee.starblues.core.PluginInfo pluginInfo = pluginOperator.uploadPlugin(uploadParam);
            if (pluginInfo == null) {
                throw new BizException(ErrCode.PLUGIN_INSTALL_FAILED);
            }

            JarFile jarFile = new JarFile(pluginInfo.getPluginPath());
            // 获取config文件在jar包中的路径
            String configFile = "classes/config.json";
            JarEntry configEntry = jarFile.getJarEntry(configFile);
            String configJson = "";

            if (configEntry != null) {
                //读取配置文件
                configJson = IoUtil.read(jarFile.getInputStream(configEntry), Charset.defaultCharset());
                log.info("configJson:{}", configJson);
            }

            //读取script.js脚本
            String scriptFile = "classes/script.js";
            JarEntry scriptEntity = jarFile.getJarEntry(scriptFile);
            String script = "";
            if (scriptEntity != null) {
                //读取脚本文件
                script = IoUtil.read(jarFile.getInputStream(scriptEntity), Charset.defaultCharset());
                log.info("script:{}", script);
            }

            PluginState pluginState = pluginInfo.getPluginState();
            if (pluginState == PluginState.STARTED) {
                plugin.setState(PluginInfo.STATE_RUNNING);
            }
            plugin.setPluginId(pluginInfo.getPluginId());
            plugin.setFile(file.getOriginalFilename());
            plugin.setConfigSchema(configJson);
            plugin.setScript(script);

            PluginDescriptor pluginDescriptor = pluginInfo.getPluginDescriptor();
            plugin.setVersion(pluginDescriptor.getPluginVersion());
            plugin.setDescription(pluginDescriptor.getDescription());
            pluginInfoData.save(plugin);
        } catch (Exception e) {
            throw new BizException(ErrCode.PLUGIN_INSTALL_FAILED, e);
        }
    }

    @Override
    public void addPlugin(PluginInfoBo plugin) {
        plugin.setState(PluginInfo.STATE_STOPPED);
        pluginInfoData.save(MapstructUtils.convert(plugin, PluginInfo.class));
    }

    @Override
    public void modifyPlugin(PluginInfoBo plugin) {
        pluginInfoData.save(plugin.to(PluginInfo.class));
    }

    @Override
    public PluginInfoVo getPlugin(Long id) {
        return pluginInfoData.findById(id).to(PluginInfoVo.class);
    }

    @Override
    public void deletePlugin(Long id) {
        PluginInfo byId = pluginInfoData.findById(id);
        if (byId == null || !PluginInfo.STATE_STOPPED.equals(byId.getState())) {
            throw new BizException(ErrCode.PARAMS_EXCEPTION, "请先停止插件");
        }
        String pluginId = byId.getPluginId();
        //停止卸载旧的
        com.gitee.starblues.core.PluginInfo pluginInfo = pluginOperator.getPluginInfo(pluginId);
        if (pluginInfo != null) {
            if (pluginInfo.getPluginState() == PluginState.STARTED) {
                pluginOperator.stop(pluginId);
            }
            pluginOperator.uninstall(pluginId, true, false);
        }

        pluginInfoData.deleteById(id);
    }

    @Override
    public Paging<PluginInfoVo> findPagePluginList(PageRequest<PluginInfoBo> query) {
        return pluginInfoData.findAll(query.to(PluginInfo.class)).to(PluginInfoVo.class);
    }

    @Override
    public void changeState(PluginInfoBo plugin) {
        String state = plugin.getState();
        if (!PluginInfo.STATE_RUNNING.equals(state) && !PluginInfo.STATE_STOPPED.equals(state)) {
            throw new BizException(ErrCode.PARAMS_EXCEPTION, "插件状态错误");
        }

        PluginInfo old = pluginInfoData.findById(plugin.getId());
        if (old == null) {
            throw new BizException(ErrCode.DATA_NOT_EXIST);
        }
        if (StringUtils.isBlank(old.getFile())) {
            throw new BizException(ErrCode.DATA_BLANK, "插件包为空");
        }

        String pluginId = old.getPluginId();

        com.gitee.starblues.core.PluginInfo pluginInfo = pluginOperator.getPluginInfo(pluginId);
        if (state.equals(PluginInfo.STATE_RUNNING) && pluginInfo != null && pluginInfo.getPluginState() != PluginState.STARTED) {
            //停止插
            pluginOperator.start(pluginId);
        } else if (state.equals(PluginInfo.STATE_STOPPED) && pluginInfo != null && pluginInfo.getPluginState() == PluginState.STARTED) {
            //停止插件
            pluginOperator.stop(pluginId);
        }

        old.setState(state);
        pluginInfoData.save(old);
    }

}
