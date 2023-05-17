/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.temporal.es.document;

import cc.iotkit.model.device.message.ThingModelMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ThingModelMessageMapper {
    ThingModelMessageMapper M = Mappers.getMapper(ThingModelMessageMapper.class);

    ThingModelMessage toDto(DocThingModelMessage vo);

    DocThingModelMessage toVo(ThingModelMessage dto);
}
