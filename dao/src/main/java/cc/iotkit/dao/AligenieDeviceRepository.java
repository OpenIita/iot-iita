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

import cc.iotkit.model.aligenie.AligenieDevice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AligenieDeviceRepository extends ElasticsearchRepository<AligenieDevice, String> {

    void deleteByUid(String uid);

    List<AligenieDevice> findByUid(String uid);

    AligenieDevice findByUidAndDeviceId(String uid, String deviceId);

    List<AligenieDevice> findByDeviceId(String deviceId);

}
