/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.service;

import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.data.dao.ThingModelRepository;
import cc.iotkit.data.service.convert.ThingModelMapper;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.product.ThingModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class ThingModelDataImpl implements IThingModelData {

    @Autowired
    private ThingModelRepository thingModelRepository;

    @Override
    public ThingModel findById(String s) {
        return ThingModelMapper.toDtoFix(thingModelRepository.findById(s).orElse(null));
    }

    @Override
    public ThingModel save(ThingModel data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        thingModelRepository.save(ThingModelMapper.toVoFix(data));
        return data;
    }

    @Override
    public ThingModel add(ThingModel data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        thingModelRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<ThingModel> findAll() {
        return null;
    }

    @Override
    public Paging<ThingModel> findAll(int page, int size) {
        return null;
    }
}
