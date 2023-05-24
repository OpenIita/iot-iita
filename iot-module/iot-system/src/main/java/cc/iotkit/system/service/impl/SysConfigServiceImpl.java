package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.constant.CacheNames;

import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.redis.utils.CacheUtils;
import cc.iotkit.common.service.ConfigService;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.SpringUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.system.ISysConfigData;
import cc.iotkit.model.system.SysConfig;
import cc.iotkit.system.dto.bo.SysConfigBo;
import cc.iotkit.system.dto.vo.SysConfigVo;

import cn.hutool.core.util.ObjectUtil;
import cc.iotkit.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cc.iotkit.common.api.Paging;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数配置 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysConfigServiceImpl implements ISysConfigService, ConfigService {

    @Autowired
    private ISysConfigData sysConfigData;

    @Override
    public Paging<SysConfigVo> selectPageConfigList(PageRequest<SysConfigBo> query) {
        SysConfig sysConfig = MapstructUtils.convert(query.getData(), SysConfig.class);
        return MapstructUtils.convert(sysConfigData.findConfigs(sysConfig), SysConfigVo.class);
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfigVo selectConfigById(Long configId) {
        return MapstructUtils.convert(sysConfigData.findById(configId), SysConfigVo.class);
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
//        SysConfig retConfig = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
//            .eq(SysConfig::getConfigKey, "sys.account.registerUser")
//            .eq(TenantHelper.isEnable(),SysConfig::getTenantId, tenantId));
//        if (ObjectUtil.isNull(retConfig)) {
//            return false;
//        }
//        return Convert.toBool(retConfig.getConfigValue());
        return false;
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfigVo> selectConfigList(SysConfigBo config) {
//        LambdaQueryWrapper<SysConfig> lqw = buildQueryWrapper(config);
//        return baseMapper.selectVoList(lqw);
        return new ArrayList<>();
    }

//    private LambdaQueryWrapper<SysConfig> buildQueryWrapper(SysConfigBo bo) {
//        Map<String, Object> params = bo.getParams();
//        LambdaQueryWrapper<SysConfig> lqw = Wrappers.lambdaQuery();
//        lqw.like(StringUtils.isNotBlank(bo.getConfigName()), SysConfig::getConfigName, bo.getConfigName());
//        lqw.eq(StringUtils.isNotBlank(bo.getConfigType()), SysConfig::getConfigType, bo.getConfigType());
//        lqw.like(StringUtils.isNotBlank(bo.getConfigKey()), SysConfig::getConfigKey, bo.getConfigKey());
//        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
//            SysConfig::getCreateTime, params.get("beginTime"), params.get("endTime"));
//        return lqw;
//    }

    /**
     * 新增参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    @Override
    public String insertConfig(SysConfigBo bo) {
//        SysConfig config = MapstructUtils.convert(bo, SysConfig.class);
//        int row = baseMapper.insert(config);
//        if (row > 0) {
//            return config.getConfigValue();
//        }
        throw new BizException("操作失败");
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
//        int row = 0;
//        SysConfig config = MapstructUtils.convert(bo, SysConfig.class);
//        if (config.getId() != null) {
//            SysConfig temp = sysConfigData.selectById(config.getConfigId());
//            if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
//                CacheUtils.evict(CacheNames.SYS_CONFIG, temp.getConfigKey());
//            }
//            row = baseMapper.updateById(config);
//        } else {
//            row = baseMapper.update(config, new LambdaQueryWrapper<SysConfig>()
//                    .eq(SysConfig::getConfigKey, config.getConfigKey()));
//        }
//        if (row > 0) {
//            return config.getConfigValue();
//        }
//        throw new BizException("操作失败");
        SysConfig config = MapstructUtils.convert(bo, SysConfig.class);
        if (config.getId() == null) {
            SysConfig old = sysConfigData.findByConfigKey(bo.getConfigKey());
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
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig old = sysConfigData.findById(configId);
            if (StringUtils.equals(UserConstants.YES, old.getConfigType())) {
                throw new BizException(String.format("内置参数【%1$s】不能删除 ", old.getConfigKey()));
            }
//            CacheUtils.evict(CacheNames.SYS_CONFIG, config.getConfigKey());
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
    @Override
    public String getConfigValue(String configKey) {
        return SpringUtils.getAopProxy(this).selectConfigByKey(configKey);
    }

}
