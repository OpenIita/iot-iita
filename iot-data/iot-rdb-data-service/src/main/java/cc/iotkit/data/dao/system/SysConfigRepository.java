/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.dao.system;

import cc.iotkit.data.model.system.TbSysConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysConfigRepository extends JpaRepository<TbSysConfig, Long>, QueryDslPredicateExecutor<TbSysConfig> {

}
