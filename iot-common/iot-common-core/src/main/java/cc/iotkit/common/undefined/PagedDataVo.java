package cc.iotkit.common.undefined;

import lombok.Data;

import java.util.List;

/**
 * 分页数据包装类
 * @author sjg
 */
@Data
public class PagedDataVo<T> {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;


}
