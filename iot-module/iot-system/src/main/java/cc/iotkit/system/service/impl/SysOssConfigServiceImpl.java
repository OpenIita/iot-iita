/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.CacheNames;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.oss.constant.OssConstant;
import cc.iotkit.common.redis.utils.CacheUtils;
import cc.iotkit.common.redis.utils.RedisUtils;
import cc.iotkit.common.tenant.helper.TenantHelper;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StreamUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.system.ISysOssConfigData;
import cc.iotkit.model.system.SysOssConfig;
import cc.iotkit.system.dto.bo.SysOssConfigBo;
import cc.iotkit.system.dto.vo.SysOssConfigVo;
import cc.iotkit.system.service.ISysOssConfigService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 对象存储配置Service业务层处理
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysOssConfigServiceImpl implements ISysOssConfigService {

    private final ISysOssConfigData baseData;

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    private void checkOssConfig() {
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if(configKey==null){
            init();
        }
    }

    /**
     * 项目启动时，初始化参数到缓存，加载配置类
     */
    @Override
    public void init() {
        List<SysOssConfig> list = baseData.findAll();
        List<SysOssConfig> notEmptyTenantIdList = list.stream().filter(item -> StringUtils.isNotBlank(item.getTenantId())).collect(Collectors.toList());
        Map<String, List<SysOssConfig>> map = StreamUtils.groupByKey(notEmptyTenantIdList, SysOssConfig::getTenantId);
        try {
            for (Map.Entry<String, List<SysOssConfig>> stringListEntry : map.entrySet()) {
                TenantHelper.setDynamic(stringListEntry.getKey());
                for (SysOssConfig config : stringListEntry.getValue()) {
                    String configKey = config.getConfigKey();
                    if ("0".equals(config.getStatus())) {
                        RedisUtils.setCacheObject(OssConstant.DEFAULT_CONFIG_KEY, configKey);
                    }
                    CacheUtils.put(CacheNames.SYS_OSS_CONFIG, config.getConfigKey(), JsonUtils.toJsonString(config));
                }
            }
        } finally {
            TenantHelper.clearDynamic();
        }
    }

    @Override
    public SysOssConfigVo queryById(Long ossConfigId) {
        return MapstructUtils.convert(baseData.findById(ossConfigId), SysOssConfigVo.class);
    }

    @Override
    public Paging<SysOssConfigVo> queryPageList(PageRequest<SysOssConfigBo> query) {
        return baseData.findAll(query.to(SysOssConfig.class)).to(SysOssConfigVo.class);
    }


    @Override
    public Boolean insertByBo(SysOssConfigBo bo) {
        SysOssConfig config = MapstructUtils.convert(bo, SysOssConfig.class);
        validEntityBeforeSave(config);
        SysOssConfig save = baseData.save(config);
        if (ObjectUtil.isNotNull(save)) {
            CacheUtils.put(CacheNames.SYS_OSS_CONFIG, config.getConfigKey(), JsonUtils.toJsonString(config));
        }
        return true;
    }

    @Override
    public Boolean updateByBo(SysOssConfigBo bo) {
        SysOssConfig config = MapstructUtils.convert(bo, SysOssConfig.class);
        validEntityBeforeSave(config);
        SysOssConfig save = baseData.save(config);
        return true;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOssConfig entity) {
        if (StringUtils.isNotEmpty(entity.getConfigKey())
                && !checkConfigKeyUnique(entity)) {
            throw new BizException("操作配置'" + entity.getConfigKey() + "'失败, 配置key已存在!");
        }
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (Objects.equals(Boolean.TRUE, isValid) && CollUtil.containsAny(ids, OssConstant.SYSTEM_DATA_IDS)) {
            throw new BizException("系统内置, 不可删除!");
        }
        List<SysOssConfig> list = CollUtil.newArrayList();
        for (Long configId : ids) {
            SysOssConfig config = baseData.findById(configId);
            list.add(config);
        }
        // TODO: 2021/8/13 删除数据校验
        baseData.deleteByIds(ids);
        list.forEach(sysOssConfig ->
                CacheUtils.evict(CacheNames.SYS_OSS_CONFIG, sysOssConfig.getConfigKey()));
        return true;
    }

    /**
     * 判断configKey是否唯一
     */
    private boolean checkConfigKeyUnique(SysOssConfig sysOssConfig) {
        long ossConfigId = ObjectUtil.isNull(sysOssConfig.getId()) ? -1L : sysOssConfig.getId();
        SysOssConfig q = new SysOssConfig();
        q.setConfigKey(sysOssConfig.getConfigKey());
        SysOssConfig info = baseData.findOneByCondition(q);
        return !ObjectUtil.isNotNull(info) || info.getId() == ossConfigId;
    }

    /**
     * 启用禁用状态
     */
    @Override
    public int updateOssConfigStatus(SysOssConfigBo bo) {
        SysOssConfig old = baseData.findById(bo.getId());
        old.setStatus(bo.getStatus());
        baseData.save(old);
        RedisUtils.deleteObject(OssConstant.DEFAULT_CONFIG_KEY);
        return 0;
    }

}
