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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.data.dao.ThingModelRepository;
import cc.iotkit.data.model.TbThingModel;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.product.ThingModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class ThingModelDataImpl implements IThingModelData, IJPACommData<ThingModel, String> {

    @Autowired
    private ThingModelRepository thingModelRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return thingModelRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbThingModel.class;
    }

    @Override
    public Class getTClass() {
        return ThingModel.class;
    }

    @Override
    public ThingModel findById(String s) {
        return MapstructUtils.convert(thingModelRepository.findById(s).orElse(null),ThingModel.class);
    }


    @Override
    public ThingModel save(ThingModel data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        thingModelRepository.save(MapstructUtils.convert(data, TbThingModel.class));
        return data;
    }



    @Override
    public void deleteById(String s) {
        thingModelRepository.deleteById(s);
    }




}
