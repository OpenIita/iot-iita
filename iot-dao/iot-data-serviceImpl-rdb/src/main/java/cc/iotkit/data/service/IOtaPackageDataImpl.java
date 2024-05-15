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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.IOtaPackageRepository;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.data.model.TbOtaPackage;
import cc.iotkit.model.ota.OtaPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:53
 * @Description:
 */
@Primary
@Service
@RequiredArgsConstructor
public class IOtaPackageDataImpl implements IOtaPackageData, IJPACommData<OtaPackage, Long> {

    private final IOtaPackageRepository iOtaPackageRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return iOtaPackageRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbOtaPackage.class;
    }

    @Override
    public Class getTClass() {
        return OtaPackage.class;
    }

    @Override
    public List<OtaPackage> findAll() {
        return iOtaPackageRepository.findAll().stream().map(e -> MapstructUtils.convert(e, OtaPackage.class)).collect(Collectors.toList());
    }

    @Override
    public Paging<OtaPackage> findAll(PageRequest<OtaPackage> pageRequest) {
        Page<TbOtaPackage> tbOtaPackages = iOtaPackageRepository.findAll(Pageable.ofSize(pageRequest.getPageSize()).withPage(pageRequest.getPageNum() - 1));
        return new Paging<>(tbOtaPackages.getTotalElements(), tbOtaPackages.getContent().stream().map(e -> MapstructUtils.convert(e, OtaPackage.class)).collect(Collectors.toList()));
    }
}
