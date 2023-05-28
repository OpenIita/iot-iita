package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.data.system.ISysTenantData;
import cc.iotkit.system.dto.bo.SysTenantBo;
import cc.iotkit.system.dto.vo.SysTenantVo;
import cc.iotkit.system.service.ISysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 租户Service业务层处理
 *
 * @author Michelle.Chung
 */
@RequiredArgsConstructor
@Service
public class SysTenantServiceImpl implements ISysTenantService {

    private final ISysTenantData sysTenantData;

    @Override
    public SysTenantVo queryById(Long id) {
        return null;
    }

    @Override
    public SysTenantVo queryByTenantId(String tenantId) {
        return null;
    }

    @Override
    public Paging<SysTenantVo> queryPageList(SysTenantBo bo, PageRequest<?> query) {
        return null;
    }

    @Override
    public List<SysTenantVo> queryList(SysTenantBo bo) {
        return null;
    }

    @Override
    public Boolean insertByBo(SysTenantBo bo) {
        return null;
    }

    @Override
    public Boolean updateByBo(SysTenantBo bo) {
        return null;
    }

    @Override
    public int updateTenantStatus(SysTenantBo bo) {
        return 0;
    }

    @Override
    public void checkTenantAllowed(String tenantId) {

    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return null;
    }

    @Override
    public boolean checkCompanyNameUnique(SysTenantBo bo) {
        return false;
    }

    @Override
    public boolean checkAccountBalance(String tenantId) {
        return false;
    }

    @Override
    public boolean checkExpireTime(String tenantId) {
        return false;
    }

    @Override
    public Boolean syncTenantPackage(String tenantId, String packageId) {
        return null;
    }
}
