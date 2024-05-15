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

import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysOssConfigRepository;
import cc.iotkit.data.model.TbSysOssConfig;
import cc.iotkit.data.system.ISysOssConfigData;
import cc.iotkit.model.system.SysOssConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author：tfd
 * @Date：2023/5/31 15:24
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysOssConfigDataImpl implements ISysOssConfigData, IJPACommData<SysOssConfig, Long> {

    private final SysOssConfigRepository operLogRepository;


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public JpaRepository getBaseRepository() {
        return operLogRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysOssConfig.class;
    }

    @Override
    public Class getTClass() {
        return SysOssConfig.class;
    }


}
