/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysUserRoleRepository;
import cc.iotkit.data.model.TbSysUserRole;
import cc.iotkit.data.system.ISysUserRoleData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysUserRole;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.iotkit.data.model.QTbSysUserRole.tbSysUserRole;

/**
 * @Author：tfd
 * @Date：2023/5/30 16:36
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysUserRoleDataImpl implements ISysUserRoleData, IJPACommData<SysUserRole, Long> {

    private final SysUserRoleRepository sysUserRoleRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public JpaRepository getBaseRepository() {
        return sysUserRoleRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysUserRole.class;
    }

    @Override
    public Class getTClass() {
        return SysUserRole.class;
    }

    @Override
    public int deleteByUserId(Long userId) {
        return sysUserRoleRepository.deleteAllByUserId(userId);
    }

    @Override
    public long countUserRoleByRoleId(Long roleId) {
        return sysUserRoleRepository.count(tbSysUserRole.roleId.eq(roleId));
    }

    @Override
    public long insertBatch(List<SysUserRole> list) {
        return sysUserRoleRepository.saveAll(MapstructUtils.convert(list,TbSysUserRole.class)).size();
    }

    @Override
    public long delete(Long roleId, List<Long> userIds) {
        return jpaQueryFactory.delete(tbSysUserRole).where(PredicateBuilder.instance()
                .and(tbSysUserRole.roleId.eq(roleId))
                .and(tbSysUserRole.userId.in(userIds))
                .build()).execute();
    }

}
