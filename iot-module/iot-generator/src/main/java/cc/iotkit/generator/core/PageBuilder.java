package cc.iotkit.generator.core;

import cc.iotkit.common.api.PageRequest;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;

/**
 * 分页查询实体类
 *
 * @author Lion Li
 */


public class PageBuilder implements Serializable {


   public static Page build(PageRequest pageRequest) {
      Integer pageNum = ObjectUtil.defaultIfNull(pageRequest.getPageNum(), PageQuery.DEFAULT_PAGE_NUM);
      Integer pageSize = ObjectUtil.defaultIfNull(pageRequest.getPageSize(), PageQuery.DEFAULT_PAGE_SIZE);
      return new Page(pageNum, pageSize);
   }

}
