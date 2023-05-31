package cc.iotkit.data.util;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.PageRequestEmpty;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.model.TbSysConfig;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.system.SysConfig;
import cn.hutool.core.collection.CollUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

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
    List<Order> orders = getOrders(request);
    if(CollUtil.isNotEmpty(orders)){
      return toPageable(request, Sort.by(orders));
    }
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

  private static List<Order> getOrders(PageRequest pageRequest) {
    List<Order> orders = new ArrayList<>();
    Map<String,String> sortMap = pageRequest.getSortMap();
    if (CollUtil.isNotEmpty(sortMap)){
      sortMap.forEach((k,v) -> {
        orders.add(new Order(Direction.ASC, k));
      });
    }
    return orders;
  }

  public static Paging<SysConfig> toPaging(Page all, Class clz) {
    return new Paging<>(all.getTotalElements(),
            MapstructUtils.convert(all.getContent(), clz));
  }
}
