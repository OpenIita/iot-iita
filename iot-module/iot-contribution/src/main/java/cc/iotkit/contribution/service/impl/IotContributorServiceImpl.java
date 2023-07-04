package cc.iotkit.contribution.service.impl;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cc.iotkit.contribution.dto.bo.IotContributorBo;
import cc.iotkit.contribution.dto.vo.IotContributorVo;
import cc.iotkit.contribution.model.IotContributor;
import cc.iotkit.contribution.service.IIotContributorService;
import cc.iotkit.contribution.data.IIotContributorData;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import cc.iotkit.common.exception.BizException;


/**
 * 贡献者Service业务层处理
 *
 * @author Lion Li
 * @date 2023-07-04
 */
@RequiredArgsConstructor
@Service
public class IotContributorServiceImpl implements IIotContributorService {

    private final IIotContributorData baseData;

    /**
     * 查询贡献者
     */
    @Override
    public IotContributorVo queryById(Long id){
        return MapstructUtils.convert(baseData.findById(id), IotContributorVo.class);
    }

    /**
     * 查询贡献者列表
     */
    @Override
    public Paging<IotContributorVo> queryPageList(PageRequest<IotContributorBo> pageQuery) {
        Paging<IotContributorVo> result = baseData.findAll(pageQuery.to(IotContributor.class)).to(IotContributorVo.class);
        return result;
    }

    /**
     * 查询贡献者列表
     */
    @Override
    public List<IotContributorVo> queryList(IotContributorBo bo) {

        return MapstructUtils.convert(baseData.findAllByCondition(bo.to(IotContributor.class)), IotContributorVo.class);
    }

    /**
     * 新增贡献者
     */
    @Override
    public Long insertByBo(IotContributorBo bo) {
        IotContributor add = MapstructUtils.convert(bo, IotContributor.class);
        validEntityBeforeSave(add);
        baseData.save(add);
        if (add == null) {
            throw new BizException("新增失败");
        }
        return add.getId();
    }

    /**
     * 修改贡献者
     */
    @Override
    public Boolean updateByBo(IotContributorBo bo) {
        IotContributor update = MapstructUtils.convert(bo, IotContributor.class);
        validEntityBeforeSave(update);
        IotContributor ret = baseData.save(update);
        if(ret == null){
            return false;
        }
        return true;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(IotContributor entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除贡献者
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        baseData.deleteByIds(ids);
        return true;
    }
}
