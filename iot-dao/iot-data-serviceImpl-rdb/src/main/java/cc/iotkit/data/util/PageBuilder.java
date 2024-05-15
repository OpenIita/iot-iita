/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.data.util;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.PageRequestEmpty;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cn.hutool.core.collection.CollUtil;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        orders.add(new Order(Direction.fromString("desc"), k));
      });
    }
    return orders;
  }

  public static <T> Paging<T> toPaging(Page all, Class clz) {
    return new Paging<>(all.getTotalElements(),
            MapstructUtils.convert(all.getContent(), clz));
  }

  public static <T> Paging<T> toPaging(Page all) {
    return new Paging<>(all.getTotalElements(), all.getContent());
  }

  public static Pageable buildPageable(int page, int size) {
    return org.springframework.data.domain.PageRequest.of(page, size);
  }

  public static <T> Paging<T> queryResults2Page(QueryResults queryResults, Class clz) {
    return new Paging<>(queryResults.getTotal(), MapstructUtils.convert(queryResults.getResults(), clz));

  }
}
