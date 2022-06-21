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

import cc.iotkit.model.space.SpaceDevice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SpaceDeviceRepository extends ElasticsearchRepository<SpaceDevice, String> {

    List<SpaceDevice> findByUidOrderByUseAtDesc(String uid);

    List<SpaceDevice> findByUidOrderByAddAtDesc(String uid);

    List<SpaceDevice> findByUid(String uid);

    List<SpaceDevice> findBySpaceIdOrderByAddAtDesc(String spaceId);

    List<SpaceDevice> findByUidAndSpaceIdOrderByAddAtDesc(String uid, String spaceId);

    SpaceDevice findByDeviceId(String deviceId);

    SpaceDevice findByDeviceIdAndUid(String deviceId, String uid);
}
