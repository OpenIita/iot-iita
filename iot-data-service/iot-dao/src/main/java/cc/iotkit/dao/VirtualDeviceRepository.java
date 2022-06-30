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

import cc.iotkit.model.device.VirtualDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface VirtualDeviceRepository extends ElasticsearchRepository<VirtualDevice, String> {

    Page<VirtualDevice> findByUid(String uid, Pageable pageable);

    List<VirtualDevice> findByUidAndState(String uid, String state);

    List<VirtualDevice> findByTriggerAndState(String trigger, String state);

}
