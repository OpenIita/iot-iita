package cc.iotkit.common.api;

import java.io.Serializable;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:15
 * @modificed by:
 */
public class PageRequest<T> extends Request<T> implements Serializable {
  private Integer pageNo = 1;
  private Integer pageSize = 20;

  public PageRequest() {
  }

  public Integer getPageNo() {
    return this.pageNo;
  }

  public Integer getPageSize() {
    return this.pageSize;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }
}
