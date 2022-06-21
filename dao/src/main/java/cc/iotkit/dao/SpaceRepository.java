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

import cc.iotkit.model.space.Space;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SpaceRepository extends ElasticsearchRepository<Space, String> {

    List<Space> findByUidOrderByCreateAtDesc(String uid);

    List<Space> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId);

    List<Space> findByHomeId(String homeId);

}
