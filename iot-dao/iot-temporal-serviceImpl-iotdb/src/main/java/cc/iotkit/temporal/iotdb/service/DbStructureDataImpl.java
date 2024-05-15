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
package cc.iotkit.temporal.iotdb.service;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.temporal.IDbStructureData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class DbStructureDataImpl implements IDbStructureData {

    @SneakyThrows
    @Override
    public void defineThingModel(ThingModel thingModel) {
        //无须处理，自动创建
    }

    @Override
    public void updateThingModel(ThingModel thingModel) {
        throw new BizException(ErrCode.UNSUPPORTED_OPERATION_EXCEPTION);
    }

    /**
     * 初始化其它数据结构
     */
    @Override
    @PostConstruct
    public void initDbStructure() {

    }

}
