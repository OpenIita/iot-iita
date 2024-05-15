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
package cc.iotkit.temporal.es.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.model.device.VirtualDeviceLog;
import cc.iotkit.temporal.IVirtualDeviceLogData;
import cc.iotkit.temporal.es.dao.VirtualDeviceLogRepository;
import cc.iotkit.temporal.es.document.DocVirtualDeviceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class VirtualDeviceLogDataImpl implements IVirtualDeviceLogData {

    @Autowired
    private VirtualDeviceLogRepository virtualDeviceLogRepository;

    @Override
    public Paging<VirtualDeviceLog> findByVirtualDeviceId(String virtualDeviceId, int page, int size) {
        Page<DocVirtualDeviceLog> paged = virtualDeviceLogRepository
                .findByVirtualDeviceId(virtualDeviceId,
                        Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(), paged.getContent().stream()
                .map(o -> MapstructUtils.convert(o, VirtualDeviceLog.class))
                .collect(Collectors.toList()));
    }

    @Override
    public void add(VirtualDeviceLog log) {
        virtualDeviceLogRepository.save(MapstructUtils.convert(log, DocVirtualDeviceLog.class));
    }
}
