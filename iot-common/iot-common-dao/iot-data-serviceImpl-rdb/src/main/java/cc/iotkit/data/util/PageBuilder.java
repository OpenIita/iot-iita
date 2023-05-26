package cc.iotkit.data.util;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.PageRequestEmpty;
import cc.iotkit.model.system.SysConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/26 17:14
 * @modificed by:
 */
public class PageBuilder {

  public static Pageable toPageable(PageRequest<?> request, Sort.Direction direction, String... properties) {
    return (Pageable)(request.getPageSize() <= 0 ? Pageable.unpaged() : org.springframework.data.domain.PageRequest.of(request.getPageNum() - 1, request.getPageSize(), direction, properties));
  }

  public static Pageable toPageable(PageRequest<?> request) {
    return (Pageable)(request.getPageSize() <= 0 ? Pageable.unpaged() : org.springframework.data.domain.PageRequest.of(request.getPageNum() - 1, request.getPageSize()));
  }

  public static Pageable toPageable(PageRequestEmpty request) {
    return (Pageable)(request.getPageNum() <= 0 ? Pageable.unpaged() : org.springframework.data.domain.PageRequest.of(request.getPageNum() - 1, request.getPageSize()));
  }

  public static Pageable toPageable(PageRequestEmpty request, Sort.Direction direction, String... properties) {
    return (Pageable)(request.getPageNum() <= 0 ? Pageable.unpaged() : org.springframework.data.domain.PageRequest.of(request.getPageNum() - 1, request.getPageSize(), direction, properties));
  }

  public static Pageable toPageable(PageRequest<?> request, Sort sort) {
    return (Pageable)(request.getPageSize() <= 0 ? Pageable.unpaged() : org.springframework.data.domain.PageRequest.of(request.getPageNum() - 1, request.getPageSize(), sort));
  }

}
