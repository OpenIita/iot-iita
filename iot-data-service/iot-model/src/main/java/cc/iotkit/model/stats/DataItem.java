package cc.iotkit.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计的数据项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataItem {

    /**
     * 数据项名
     */
    private String name;

    /**
     * 数据项值
     */
    private Object value;

}
