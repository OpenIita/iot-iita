package cc.iotkit.data.system;

import cc.iotkit.data.ICommonData;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.system.SysLogininfor;

import java.util.List;

/**
 * 登录记录数据接口
 *
 * @author sjg
 */
public interface ISysLogininforData extends ICommonData<SysLogininfor, Long> {

    /**
     * 按条件查询
     */
    List<SysLogininfor> findByConditions(SysLogininfor cond);

    /**
     * 按条件分页查询
     */
    Paging<SysLogininfor> findByConditions(SysLogininfor cond, int page, int size);

    /**
     * 按租户清除登录日志
     *
     * @param tenantId 租户id
     */
    void deleteByTenantId(String tenantId);

    /**
     * 清除所有
     */
    void deleteAll();
}
