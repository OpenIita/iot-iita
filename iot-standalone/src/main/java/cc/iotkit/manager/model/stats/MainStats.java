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
     * 上报数据数量统计
     */
    private List<TimeData> reportDataStats;

    /**
     * 按品类统计的设备数量
     */
    private List<DataItem> deviceStatsOfCategory;

}
