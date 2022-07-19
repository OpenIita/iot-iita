/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbSpaceDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceDeviceRepository extends JpaRepository<TbSpaceDevice, String> {

    List<TbSpaceDevice> findByUid(String uid);

    Page<TbSpaceDevice> findByUid(String uid, Pageable pageable);

    List<TbSpaceDevice> findByUidOrderByUseAtDesc(String uid);

    List<TbSpaceDevice> findByUidOrderByAddAtDesc(String uid);

    List<TbSpaceDevice> findBySpaceIdOrderByAddAtDesc(String spaceId);

    List<TbSpaceDevice> findByUidAndSpaceIdOrderByAddAtDesc(String uid, String spaceId);

    TbSpaceDevice findByDeviceId(String deviceId);

    TbSpaceDevice findByDeviceIdAndUid(String deviceId, String uid);
}
