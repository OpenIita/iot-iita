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

import cc.iotkit.model.device.DeviceInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DeviceInfoRepository extends ElasticsearchRepository<DeviceInfo, String> {

    DeviceInfo findByProductKeyAndDeviceName(String productKey, String deviceName);

    DeviceInfo findByDeviceId(String deviceId);

    List<DeviceInfo> findByParentId(String parentId);

    List<DeviceInfo> findByParentIdAndUid(String parentId, String uid);

    List<DeviceInfo> findByDeviceName(String deviceName);

    long countByUid(String uid);

}
