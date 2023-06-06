package cc.iotkit.common.api;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.SnowflakeIdGeneratorUtil;
import cn.hutool.core.collection.CollUtil;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

import lombok.*;

import java.io.Serializable;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:15
 * @modificed by:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequest<T> extends Request<T> implements Serializable {

  /**
   * 分页大小
   */
  @Min(1)
  @NotNull
  private Integer pageSize;

  /**
   * 当前页数
   */
  @Min(1)
  @Max(100)
  @NotNull
  private Integer pageNum;

  /**
   * 排序 key为排序字段名 value为排序方向方向desc或者asc
   */
  private Map<String,String> sortMap;



  /**
   * 当前记录起始索引 默认值
   */
  public static final int DEFAULT_PAGE_NUM = 1;

  /**
   * 每页显示记录数 默认值
   */
  public static final int DEFAULT_PAGE_SIZE = 20;


  public static <T> PageRequest<T> of(T data) {
    PageRequest<T> pageRequest = new PageRequest<>();
    pageRequest.setPageSize(DEFAULT_PAGE_SIZE);
    pageRequest.setPageNum(DEFAULT_PAGE_NUM);
    pageRequest.setData(data);
    pageRequest.setRequestId(String.valueOf(SnowflakeIdGeneratorUtil.getInstanceSnowflake().nextId()));
    return pageRequest;
  }

  public static <DTO> PageRequest<DTO> request2PageRequest(Request<DTO> request) {
    PageRequest<DTO> pageRequest = new PageRequest<>();
    pageRequest.setData(request.getData());
    pageRequest.setPageNum(DEFAULT_PAGE_NUM);
    pageRequest.setPageSize(DEFAULT_PAGE_SIZE);
    pageRequest.setRequestId(request.getRequestId());
    return pageRequest;
  }

  public <DTO> PageRequest<DTO> to(Class<DTO> dtoClass) {
    DTO dto = MapstructUtils.convert(getData(), dtoClass);
    PageRequest<DTO> pageRequest = new PageRequest<>();
    pageRequest.setData(dto);
    pageRequest.setPageNum(this.getPageNum());
    pageRequest.setPageSize(this.getPageSize());
    pageRequest.setRequestId(this.getRequestId());
    pageRequest.setSortMap(this.getSortMap());
    return pageRequest;
  }

  public Integer getPageSize() {
    return pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
  }

  public Integer getPageNum() {
    return pageNum == null ? DEFAULT_PAGE_NUM : pageNum;
  }

  public Integer getOffset() {
    return (getPageNum() - 1) * getPageSize();
  }
}
