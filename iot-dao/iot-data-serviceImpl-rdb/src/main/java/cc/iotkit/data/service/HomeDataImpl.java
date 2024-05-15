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

import cc.iotkit.common.satoken.utils.LoginHelper;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.HomeRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IHomeData;
import cc.iotkit.data.model.TbHome;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.space.Home;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static cc.iotkit.data.model.QTbHome.tbHome;

@Primary
@Service
@RequiredArgsConstructor
public class HomeDataImpl implements IHomeData, IJPACommData<Home, Long> {

    @Autowired
    private HomeRepository homeRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public JpaRepository getBaseRepository() {
        return homeRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbHome.class;
    }

    @Override
    public Class getTClass() {
        return Home.class;
    }

    @Override
    public Home findByUserIdAndCurrent(Long userId, boolean current) {
        return MapstructUtils.convert(homeRepository.findByUserIdAndCurrent(userId, current), Home.class);
    }

    @Override
    public List<Home> findByUserId(Long userId) {
        return MapstructUtils.convert(homeRepository.findByUserId(userId), Home.class);
    }

    @Override
    public boolean checkHomeNameUnique(Home home) {
        final TbHome ret = jpaQueryFactory.select(tbHome).from(tbHome)
                .where(PredicateBuilder.instance()
                        .and(tbHome.name.eq(home.getName()))
                        .and(tbHome.userId.eq(LoginHelper.getUserId()))
                        .build()).fetchOne();
        return Objects.isNull(ret);
    }


}
