package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.CacheNames;
import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.redis.utils.CacheUtils;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.SpringUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.system.ISysConfigData;
import cc.iotkit.model.system.SysConfig;
import cc.iotkit.system.dto.bo.SysConfigBo;
import cc.iotkit.system.dto.vo.SysConfigVo;
import cc.iotkit.system.service.ISysConfigService;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysConfigServiceImpl implements ISysConfigService {

    @Autowired
    private ISysConfigData sysConfigData;

    @Override
    public Paging<SysConfigVo> selectPageConfigList(PageRequest<SysConfigBo> query) {
        return sysConfigData.findAllByConditions(query.to(SysConfig.class)).to(SysConfigVo.class);
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfigVo selectConfigById(Long configId) {
        return sysConfigData.findById(configId).to(SysConfigVo.class);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        SysConfig sysConfig = sysConfigData.findByConfigKey(configKey);
        if (ObjectUtil.isNotNull(sysConfig)) {
            return sysConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取注册开关
     *
     * @param tenantId 租户id
     * @return true开启，false关闭
     */
    @Override
    public boolean selectRegisterEnabled(String tenantId) {
        SysConfig query = new SysConfig();
        query.setConfigKey("sys.account.registerUser");
        SysConfig retConfig = sysConfigData.findOneByCondition(query);

        if (ObjectUtil.isNull(retConfig)) {
            return false;
        }
        return Convert.toBool(retConfig.getConfigValue());
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfigVo> selectConfigList(SysConfigBo config) {

        List<SysConfig> allByCondition = sysConfigData.findAllByCondition(MapstructUtils.convert(config, SysConfig.class));
        return MapstructUtils.convert(allByCondition, SysConfigVo.class);
    }

    /**
     * 新增参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    @Override
    public String insertConfig(SysConfigBo bo) {
        SysConfig to = bo.to(SysConfig.class);
        sysConfigData.save(to);
        return String.valueOf(to.getId());
    }

    /**
     * 修改参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
//    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#bo.configKey")
    @Override
    public String updateConfig(SysConfigBo bo) {
        SysConfig config = MapstructUtils.convert(bo, SysConfig.class);
        if (config.getId() == null) {
            SysConfig old = sysConfigData.findByConfigKey(config.getConfigKey());
            if (old == null) {
                throw new BizException("操作失败,key不存在");
            }
            config.setId(old.getId());
        }

        sysConfigData.save(config);
        return config.getConfigValue();
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(List<Long> configIds) {
        for (Long configId : configIds) {
            SysConfig old = sysConfigData.findById(configId);
            if (StringUtils.equals(UserConstants.YES, old.getConfigType())) {
                throw new BizException(String.format("内置参数【%1$s】不能删除 ", old.getConfigKey()));
            }
            CacheUtils.evict(CacheNames.SYS_CONFIG, old.getConfigKey());
        }
        sysConfigData.deleteByIds(configIds);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        CacheUtils.clear(CacheNames.SYS_CONFIG);
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfigBo config) {
        long configId = ObjectUtil.isNull(config.getId()) ? -1L : config.getId();
        SysConfig old = sysConfigData.findByConfigKey(config.getConfigKey());
        return !ObjectUtil.isNotNull(old) || old.getId() == configId;
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    public String getConfigValue(String configKey) {
        return SpringUtils.getAopProxy(this).selectConfigByKey(configKey);
    }

}
