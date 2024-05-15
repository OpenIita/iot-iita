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
package cc.iotkit.manager.model.stats;

import cc.iotkit.model.stats.DataItem;
import cc.iotkit.model.stats.TimeData;
import lombok.Data;

import java.util.List;

/**
 * 首页数据统计
 */
@Data
public class MainStats {

    /**
     * 品类数量
     */
    private long categoryTotal;

    /**
     * 产品数量
     */
    private long productTotal;

    /**
     * 设备数量
     */
    private long deviceTotal;

    /**
     * 上报数量
     */
    private long reportTotal;

    /**
     * 在线数量
     */
    private long onlineTotal;

    /**
     * 离线数量
     */
    private long offlineTotal;

    /**
     * 待激活设备
     */
    private long neverOnlineTotal;

    /**
     * 上报数据数量统计
     */
    private List<TimeData> reportDataStats;


    /**
     * 上行数据数量统计
     */
    private List<TimeData> deviceUpMessageStats;

    /**
     * 下行数据数量统计
     */
    private List<TimeData> deviceDownMessageStats;


    /**
     * 按品类统计的设备数量
     */
    private List<DataItem> deviceStatsOfCategory;




}
