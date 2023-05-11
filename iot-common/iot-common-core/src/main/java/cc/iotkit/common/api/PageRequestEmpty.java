package cc.iotkit.common.api;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:16
 * @modificed by:
 */
public class PageRequestEmpty {
  private Integer pageNo = 1;
  private  Integer pageSize = 20;

  public PageRequestEmpty() {
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
