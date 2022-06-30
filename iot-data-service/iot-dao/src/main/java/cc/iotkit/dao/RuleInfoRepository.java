/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.dao;

import cc.iotkit.model.rule.RuleInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface RuleInfoRepository extends ElasticsearchRepository<RuleInfo, String> {

    List<RuleInfo> findByUidAndType(String uid, String type);

    Page<RuleInfo> findByUidAndType(String uid, String type, Pageable pageable);

    Page<RuleInfo> findByType(String type, Pageable pageable);

}
