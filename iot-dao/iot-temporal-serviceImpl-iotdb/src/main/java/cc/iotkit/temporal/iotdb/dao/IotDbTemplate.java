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

package cc.iotkit.temporal.iotdb.dao;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.isession.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.apache.iotdb.tsfile.read.common.Field;
import org.apache.iotdb.tsfile.read.common.RowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sjg
 */
@Slf4j
@Component
@Data
public class IotDbTemplate {

    @Autowired
    private SessionPool sessionPool;

    private static String group = "root.iotkit";

    private String getPath(String productKey, String deviceId) {
        return group + "." + productKey + "." + deviceId;
    }

    /**
     * 对齐插入时序序列
     * @param productKey 产品key
     * @param deviceId  设备id
     * @param time  数据时间
     * @param data  数据键值对
     */
    @SneakyThrows
    public void insert(String productKey, String deviceId, long time, Map<String, Object> data) {
        String path = getPath(productKey, deviceId);
        List<String> measurements = new ArrayList<>();
        // 需要服务器做类型判断
        List<String> values = new ArrayList<>();
        for (String key : data.keySet()) {
            measurements.add(key);
            values.add(String.valueOf(data.get(key)));
        }
        //对齐插入，使用PREVIOUS填充查询
        sessionPool.insertAlignedRecord(path, time, measurements, values);
    }

    @SneakyThrows
    public List<Map<String,Object>> query(String productKey, String deviceId,long startTime,long endTime) {
        List<Map<String,Object>> list = new ArrayList<>();
        SessionDataSetWrapper dataSetWrapper = sessionPool.executeRawDataQuery(
                List.of(getPath(productKey,deviceId)),startTime,endTime,5000);
        while (dataSetWrapper.hasNext()) {
            RowRecord record = dataSetWrapper.next();
            Map<String, Object> data = new HashMap<>(record.getFields().size() + 1);
            long time = record.getTimestamp();
            data.put("time", time);
            for (Field field : record.getFields()) {
                field.getObjectValue(field.getDataType());
            }
            list.add(data);
        }
        return list;
    }

}
