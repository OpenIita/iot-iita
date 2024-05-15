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

import cc.iotkit.data.dao.DeviceOtaDetailRepository;
import cc.iotkit.data.dao.DeviceOtaInfoRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IDeviceOtaDetailData;
import cc.iotkit.data.manager.IDeviceOtaInfoData;
import cc.iotkit.data.model.TbDeviceOtaDetail;
import cc.iotkit.data.model.TbDeviceOtaInfo;
import cc.iotkit.model.ota.DeviceOtaDetail;
import cc.iotkit.model.ota.DeviceOtaInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author: 石恒
 * @Date: 2023/6/15 22:19
 * @Description:
 */
@Primary
@Service
@RequiredArgsConstructor
public class DeviceOtaDetailDataImpl implements IDeviceOtaDetailData, IJPACommData<DeviceOtaDetail, Long> {

    private final DeviceOtaDetailRepository deviceOtaDetailRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public JpaRepository getBaseRepository() {
        return deviceOtaDetailRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbDeviceOtaDetail.class;
    }

    @Override
    public Class getTClass() {
        return DeviceOtaDetail.class;
    }
}
