package cc.iotkit.contribution.service;

import cc.iotkit.contribution.dto.vo.IotContributorVo;
import cc.iotkit.contribution.dto.bo.IotContributorBo;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.PageRequest;

import java.util.Collection;
import java.util.List;

/**
 * 贡献者Service接口
 *
 * @author Lion Li
 * @date 2023-07-04
 */
public interface IIotContributorService {

    /**
     * 查询贡献者
     */
    IotContributorVo queryById(Long id);

    /**
     * 查询贡献者列表
     */
    Paging<IotContributorVo> queryPageList(PageRequest<IotContributorBo> pageQuery);

    /**
     * 查询贡献者列表
     */
    List<IotContributorVo> queryList(IotContributorBo bo);

    /**
     * 新增贡献者
     */
    Long insertByBo(IotContributorBo bo);

    /**
     * 修改贡献者
     */
    Boolean updateByBo(IotContributorBo bo);

    /**
     * 校验并批量删除贡献者信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
