package cc.iotkit.system.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.undefined.PagedDataVo;
import cc.iotkit.system.dto.bo.SysTenantPackageBo;
import cc.iotkit.system.dto.vo.SysTenantPackageVo;

import java.util.Collection;
import java.util.List;

/**
 * 租户套餐Service接口
 *
 * @author Michelle.Chung
 */
public interface ISysTenantPackageService {

    /**
     * 查询租户套餐
     */
    SysTenantPackageVo queryById(Long packageId);

    /**
     * 查询租户套餐列表
     */
    PagedDataVo<SysTenantPackageVo> queryPageList(SysTenantPackageBo bo, PageRequest<?> query);

    /**
     * 查询租户套餐已启用列表
     */
    List<SysTenantPackageVo> selectList();

    /**
     * 查询租户套餐列表
     */
    List<SysTenantPackageVo> queryList(SysTenantPackageBo bo);

    /**
     * 新增租户套餐
     */
    Boolean insertByBo(SysTenantPackageBo bo);

    /**
     * 修改租户套餐
     */
    Boolean updateByBo(SysTenantPackageBo bo);

    /**
     * 修改套餐状态
     */
    int updatePackageStatus(SysTenantPackageBo bo);

    /**
     * 校验并批量删除租户套餐信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
