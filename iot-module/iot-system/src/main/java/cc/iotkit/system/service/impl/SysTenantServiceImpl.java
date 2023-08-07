package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.CacheNames;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.SpringUtils;
import cc.iotkit.data.system.ISysTenantData;
import cc.iotkit.data.system.ISysUserData;
import cc.iotkit.model.system.SysTenant;
import cc.iotkit.system.dto.bo.SysTenantBo;
import cc.iotkit.system.dto.vo.SysTenantVo;
import cc.iotkit.system.service.ISysTenantService;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户Service业务层处理
 *
 * @author Michelle.Chung
 */
@RequiredArgsConstructor
@Service
public class SysTenantServiceImpl implements ISysTenantService {

    private final ISysTenantData sysTenantData;

    private final ISysUserData sysUserData;

    @Override
    public SysTenantVo queryById(Long id) {
        return sysTenantData.findById(id).to(SysTenantVo.class);
    }

    /**
     * 基于租户ID查询租户
     */
    @Cacheable(cacheNames = CacheNames.SYS_TENANT, key = "#tenantId")
    @Override
    public SysTenantVo queryByTenantId(String tenantId) {
        SysTenant tenant = sysTenantData.findById(Long.valueOf(tenantId));
        return MapstructUtils.convert(tenant,SysTenantVo.class);
    }

    @Override
    public Paging<SysTenantVo> queryPageList(PageRequest<SysTenantBo> query) {
        return sysTenantData.findAll(query.to(SysTenant.class)).to(SysTenantVo.class);
    }

    @Override
    public List<SysTenantVo> queryList(SysTenantBo bo) {
        return MapstructUtils.convert(sysTenantData.findAllByCondition(bo.to(SysTenant.class)),SysTenantVo.class);
    }

    @Override
    public void insertByBo(SysTenantBo bo) {
        bo.setTenantId(IdUtil.simpleUUID());
        sysTenantData.save(bo.to(SysTenant.class));
    }

    @Override
    public void updateByBo(SysTenantBo bo) {
        SysTenant tenantDataById = sysTenantData.findById(bo.getId());
        String tenantId = tenantDataById.getTenantId();
        bo.setTenantId(tenantId);
        sysTenantData.updateTenant(bo.to(SysTenant.class));
    }

    @Override
    public int updateTenantStatus(SysTenantBo bo) {
        SysTenant tenantDataById = sysTenantData.findById(bo.getId());
        tenantDataById.setStatus(bo.getStatus());
        sysTenantData.updateTenant(tenantDataById);
        return 0;
    }

    @Override
    public void checkTenantAllowed(String tenantId) {

    }

    @Override
    public void deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        sysTenantData.deleteByIds(ids);
    }

    @Override
    public boolean checkCompanyNameUnique(SysTenantBo bo) {
        return true;
    }

    @Override
    public boolean checkAccountBalance(String tenantId) {
        SysTenantVo tenant = SpringUtils.getAopProxy(this).queryByTenantId(tenantId);
        // 如果余额为-1代表不限制
        if (tenant.getAccountCount() == -1) {
            return true;
        }
        Long userNumber = sysUserData.count();
        // 如果余额大于0代表还有可用名额
        return tenant.getAccountCount() - userNumber > 0;
    }

    @Override
    public boolean checkExpireTime(String tenantId) {
        return false;
    }

    @Override
    public Boolean syncTenantPackage(String tenantId, String packageId) {
        return false;
    }
}
