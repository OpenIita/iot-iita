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
import cc.iotkit.data.dao.SysAppRepository;
import cc.iotkit.data.model.TbSysApp;
import cc.iotkit.data.system.ISysAppData;
import cc.iotkit.model.system.SysApp;
import cn.hutool.core.util.ObjectUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * 数据实现接口
 *
 * @author Lion Li
 * @date 2023-08-10
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysAppDataImpl implements ISysAppData, IJPACommData<SysApp, Long> {

    private final SysAppRepository baseRepository;

    private final JPAQueryFactory jpaQueryFactory;

//    @Override
//    public Paging<SysApp> findAll(PageRequest<SysApp> pageRequest) {
//        return PageBuilder.toPaging(baseRepository.findAll(buildQueryCondition(pageRequest.getData()), PageBuilder.toPageable(pageRequest))).to(SysApp.class);
//    }

//    private Predicate buildQueryCondition(SysApp bo) {
//        PredicateBuilder builder = PredicateBuilder.instance();
//        if(Objects.nonNull(bo)) {
//
//                        builder.and(StringUtils.isNotBlank(bo.getId()), () -> tbSysApp.ID.eq(bo.getId()));
//                        builder.and(StringUtils.isNotBlank(bo.getTenantId()), () -> tbSysApp.tenantId.eq(bo.getTenantId()));
//                        builder.and(StringUtils.isNotBlank(bo.getAppId()), () -> tbSysApp.appId.eq(bo.getAppId()));
//                        builder.and(StringUtils.isNotBlank(bo.getAppSecret()), () -> tbSysApp.appSecret.eq(bo.getAppSecret()));
//                        builder.and(StringUtils.isNotBlank(bo.getAppType()), () -> tbSysApp.appType.eq(bo.getAppType()));
//                        builder.and(StringUtils.isNotBlank(bo.getRemark()), () -> tbSysApp.REMARK.eq(bo.getRemark()));
//        }
//        return builder.build();
//    }

    @Override
    public JpaRepository getBaseRepository() {
        return baseRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysApp.class;
    }

    @Override
    public Class getTClass() {
        return SysApp.class;
    }

    @Override
    public SysApp findByAppId(String appId) {
        TbSysApp ret=baseRepository.findByAppId(appId);
        if(ObjectUtil.isNotNull(ret)){
            return MapstructUtils.convert(ret,SysApp.class);
        }else{
            return null;
        }
    }
}
