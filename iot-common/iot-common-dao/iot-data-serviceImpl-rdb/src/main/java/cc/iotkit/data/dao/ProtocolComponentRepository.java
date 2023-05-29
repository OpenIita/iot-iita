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

import cc.iotkit.data.model.TbProtocolComponent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ProtocolComponentRepository extends JpaRepository<TbProtocolComponent, String>, QuerydslPredicateExecutor<TbProtocolComponent>{

    List<TbProtocolComponent> findByState(String state);

    List<TbProtocolComponent> findByStateAndType(String state, String type);

    List<TbProtocolComponent> findByUid(String uid);

    Page<TbProtocolComponent> findByUid(String uid, Pageable pageable);

    long countByUid(String uid);

}
