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

import cc.iotkit.data.model.TbSpace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository extends JpaRepository<TbSpace, String> {

    List<TbSpace> findByUidOrderByCreateAtDesc(String uid);

    List<TbSpace> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId);

    List<TbSpace> findByHomeId(String homeId);

    List<TbSpace> findByUid(String uid);

    Page<TbSpace> findByUid(String uid, Pageable pageable);

}
