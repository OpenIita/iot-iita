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

import cc.iotkit.model.rule.RuleLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RuleLogMapper {
    RuleLogMapper M = Mappers.getMapper(RuleLogMapper.class);

    RuleLog toDto(DocRuleLog vo);

    DocRuleLog toVo(RuleLog dto);
}
