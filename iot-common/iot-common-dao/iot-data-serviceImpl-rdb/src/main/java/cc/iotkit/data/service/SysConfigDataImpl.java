package cc.iotkit.data.service;

import static cc.iotkit.data.model.QTbSysConfig.tbSysConfig;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.SysConfigRepository;
import cc.iotkit.data.model.TbSysConfig;
import cc.iotkit.data.system.ISysConfigData;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysConfig;
import cn.hutool.core.collection.CollUtil;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Primary
@Service
public class SysConfigDataImpl implements ISysConfigData {

    @Autowired
    private SysConfigRepository alertConfigRepository;


    @Override
    public SysConfig findById(Long id) {
       TbSysConfig tbSysConfig =  alertConfigRepository.findById(id).orElseThrow(() ->
           new BizException(ErrCode.DATA_NOT_EXIST));
        return MapstructUtils.convert(tbSysConfig,SysConfig.class);
    }

    @Override
    public SysConfig save(SysConfig data) {
        alertConfigRepository.save(MapstructUtils.convert(data,TbSysConfig.class));
        return data;
    }

    @Override
    public List<SysConfig> findByIds(Collection<Long> id) {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public void batchSave(List<SysConfig> data) {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public void deleteById(Long aLong) {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public void deleteByIds(Collection<Long> longs) {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public long count() {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public List<SysConfig> findAll() {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public Paging<SysConfig> findAll(PageRequest<SysConfig> pageRequest) {
        SysConfig query = pageRequest.getData();
        Predicate predicate = PredicateBuilder.instance(tbSysConfig.configId.isNotNull())
            .and(StringUtils.isNotEmpty(query.getConfigKey()),() -> tbSysConfig.configKey.eq(query.getConfigKey()))

            .build();

        List<Order> orders = new ArrayList<>();
        Map<String,String> sortMap = pageRequest.getSortMap();
        if (CollUtil.isNotEmpty(sortMap)){
            sortMap.forEach((k,v) -> {
                orders.add(new Order(Direction.ASC, k));
            });
        }
        // TODO: 2023/5/26 抽成通用工具类方法


        alertConfigRepository.findAll(predicate,PageBuilder.toPageable(pageRequest, Sort.by(orders)));



        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public List<SysConfig> findAllByCondition(SysConfig data) {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    @Override
    public SysConfig findOneByCondition(SysConfig data) {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }
}
