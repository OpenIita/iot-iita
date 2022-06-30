/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.dao;

import cc.iotkit.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CommonDao {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 通用按条件分页查找
     */
    public <T> Paging<T> pagedFind(Class<T> cls, Criteria condition, Sort.Order order, int size, int page) {
        Query query = new CriteriaQuery(condition);
        long total = elasticsearchRestTemplate.count(query, cls);
        query = query.setPageable(PageRequest.of(page - 1, size, Sort.by(order)));
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(query, cls);
        List<T> list = new ArrayList<>();
        for (SearchHit<T> searchHit : searchHits) {
            list.add(searchHit.getContent());
        }
        return new Paging<>(total, list);
    }

    /**
     * 通用按条件查询
     */
    public <T> List<T> find(Class<T> cls, Criteria condition) {
        Query query = new CriteriaQuery(condition);
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(query, cls);
        List<T> list = new ArrayList<>();
        for (SearchHit<T> searchHit : searchHits) {
            list.add(searchHit.getContent());
        }
        return list;
    }

}
