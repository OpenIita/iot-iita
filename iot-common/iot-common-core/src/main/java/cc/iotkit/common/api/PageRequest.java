package cc.iotkit.common.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:15
 * @modificed by:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageRequest<T> extends Request<T> implements Serializable {

  /**
   * 分页大小
   */
  private Integer pageSize;

  /**
   * 当前页数
   */
  private Integer pageNum;

  /**
   * 排序列
   */
  private String orderByColumn;

  /**
   * 排序的方向desc或者asc
   */
  private String isAsc;

  /**
   * 当前记录起始索引 默认值
   */
  public static final int DEFAULT_PAGE_NUM = 1;

  /**
   * 每页显示记录数 默认值 默认查全部
   */
  public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

}
