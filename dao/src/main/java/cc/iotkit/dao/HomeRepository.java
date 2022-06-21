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

import cc.iotkit.model.space.Home;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface HomeRepository extends ElasticsearchRepository<Home, String> {

    List<Home> findByUid(String uid);

    Home findByUidAndCurrent(String uid, boolean current);

    Home findByUidAndId(String uid, String id);

}
