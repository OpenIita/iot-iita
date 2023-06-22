package cc.iotkit.generator.core;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.StringUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页查询实体类
 *
 * @author Lion Li
 */


public class PageBuilder implements Serializable {


    static public Page build(PageRequest pageRequest) {
       Integer pageNum = ObjectUtil.defaultIfNull(pageRequest.getPageNum(), PageQuery.DEFAULT_PAGE_NUM);
       Integer pageSize = ObjectUtil.defaultIfNull(pageRequest.getPageSize(), PageQuery.DEFAULT_PAGE_SIZE);
       Page page = new Page(pageNum, pageSize);

       return page;
   }

}
