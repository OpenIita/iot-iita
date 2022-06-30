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

import cc.iotkit.dao.config.EmbeddedEs;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.stats.DataItem;
import cn.hutool.core.bean.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ScriptType;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DeviceDao {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Paging<DeviceInfo> find(Criteria condition, int size, int page) {
        Query query = new CriteriaQuery(condition);
        long total = elasticsearchRestTemplate.count(query, DeviceInfo.class);
        query = query.setPageable(PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createAt"))));
        SearchHits<DeviceInfo> searchHits = elasticsearchRestTemplate.search(query, DeviceInfo.class);
        List<DeviceInfo> list = new ArrayList<>();
        for (SearchHit<DeviceInfo> searchHit : searchHits) {
            list.add(searchHit.getContent());
        }

        return new Paging<>(total, list);
    }

    /**
     * 更新设备属性
     */
    public void updateProperties(String deviceId, Map<String, Object> properties) {
        if (properties == null) {
            return;
        }
        //外置es采用脚本更新
        if (EmbeddedEs.disabled) {
            Map<String, Object> param = new HashMap<>();
            param.put("property", BeanUtil.beanToMap(properties));
            param.put("keys", properties.keySet());

            UpdateQuery updateQuery = UpdateQuery.builder(new CriteriaQuery(new Criteria()
                    .and("deviceId").is(deviceId)))
                    .withParams(param)
                    .withScript("for(key in params.keys){ctx._source.property[key]=params.property[key];}")
                    .withScriptType(ScriptType.INLINE)
                    .build();
            elasticsearchRestTemplate.updateByQuery(updateQuery, IndexCoordinates.of("device_info"));
        } else {
            //内置es采用文档更新
            DeviceInfo deviceInfo = deviceInfoRepository.findByDeviceId(deviceId);
            Map<String, Object> oldProps = deviceInfo.getProperty();
            oldProps.putAll(properties);
            deviceInfoRepository.save(deviceInfo);
        }
    }

    /**
     * 更新设备标签
     */
    public void updateTag(String deviceId, DeviceInfo.Tag tag) {
        Map<String, Object> param = new HashMap<>();
        param.put("tag", BeanUtil.beanToMap(tag));

        UpdateQuery updateQuery = UpdateQuery.builder(new CriteriaQuery(new Criteria()
                .and("deviceId").is(deviceId)))
                .withParams(param)
                .withScript(String.format("ctx._source.tag.%s=params.tag", tag.getId()))
                .withScriptType(ScriptType.INLINE)
                .build();
        elasticsearchRestTemplate.updateByQuery(updateQuery, IndexCoordinates.of("device_info"));
    }

    /**
     * 设置设备标签值为空
     */
    public void setTagNull(String deviceId, String tagId) {
//        Query query = Query.query(new Criteria().and("deviceId").is(deviceId));
//        Update update = new Update();
//        update.set("tag." + tagId, null);
//        mongoTemplate.updateFirst(query, update, DeviceInfo.class);
    }

    /**
     * 获取按品类统计的用户设备数
     */
    public List<DataItem> getDeviceStatsByCategory(String uid) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(uid)) {
            queryBuilder =
                    queryBuilder.must(QueryBuilders.termQuery("uid.keyword", uid));
        }

        //先按产品分组统计
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withAggregations(
                        AggregationBuilders.terms("countByPk").field("productKey.keyword")
                                .size(1000)
                                .subAggregation(AggregationBuilders.count("count").field("productKey.keyword"))
                )
                .build();

        ElasticsearchAggregations result = (ElasticsearchAggregations) elasticsearchRestTemplate
                .search(query, DeviceInfo.class).getAggregations();
        ParsedStringTerms terms = result.aggregations().get("countByPk");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        Map<String, Long> productCount = new HashMap<>();
        for (Terms.Bucket bucket : buckets) {
            productCount.put(bucket.getKeyAsString(), bucket.getDocCount());
        }

        //取用户下产品列表
        Iterable<Product> products;
        if (StringUtils.isNotBlank(uid)) {
            products = productRepository.findByUid(uid);
        } else {
            products = productRepository.findAll();
        }
        Map<String, String> pkCateMap = new HashMap<>();
        for (Product product : products) {
            pkCateMap.put(product.getId(), product.getCategory());
        }

        //取品类列表
        Map<String, String> cateNames = new HashMap<>();
        for (Category category : categoryRepository.findAll()) {
            cateNames.put(category.getId(), category.getName());
        }

        Map<String, Long> cateStats = new HashMap<>();
        productCount.forEach((key, val) -> {
            String cateName = cateNames.get(pkCateMap.get(key));
            //按品类汇总
            long total = cateStats.getOrDefault(cateName, 0L);
            total += val;
            cateStats.put(cateName, total);
        });

        List<DataItem> items = new ArrayList<>();
        cateStats.forEach((key, val) -> {
            items.add(new DataItem(key, val));
        });

        return items;
    }

    /**
     * 获取按品类统计的设备数
     */
    public List<DataItem> getDeviceStatsByCategory() {
        return getDeviceStatsByCategory(null);
    }

    /**
     * 根据分组id查询分组下所有设备
     */
    public List<DeviceInfo> findByGroupId(String groupId) {
        Query query = new CriteriaQuery(new Criteria().and("group." + groupId).exists());
        SearchHits<DeviceInfo> searchHits = elasticsearchRestTemplate.search(query, DeviceInfo.class);
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    /**
     * 按分组id统计设备数量
     */
    public long countByGroupId(String groupId) {
        Query query = new CriteriaQuery(new Criteria().and("group." + groupId).exists());
        return elasticsearchRestTemplate.count(query, DeviceInfo.class);
    }

    /**
     * 按设备id更新设备分组
     */
    public void updateGroupByDeviceId(String deviceId, DeviceInfo.Group group) {
        Map<String, Object> param = new HashMap<>();
        param.put("group", BeanUtil.beanToMap(group));

        UpdateQuery updateQuery = UpdateQuery.builder(new CriteriaQuery(new Criteria()
                .and("deviceId").is(deviceId)))
                .withParams(param)
                .withScript(String.format("ctx._source.group.%s=params.group", group.getId()))
                .withScriptType(ScriptType.INLINE)
                .build();
        elasticsearchRestTemplate.updateByQuery(updateQuery, IndexCoordinates.of("device_info"));
    }

    /**
     * 按组id更新设备分组
     */
    public void updateGroup(String groupId, DeviceInfo.Group group) {
        Map<String, Object> param = new HashMap<>();
        param.put("group", BeanUtil.beanToMap(group));

        UpdateQuery updateQuery = UpdateQuery.builder(new CriteriaQuery(new Criteria()
                .and("group." + groupId).exists()))
                .withParams(param)
                .withScript(String.format("ctx._source.group.%s=params.group", groupId))
                .withScriptType(ScriptType.INLINE)
                .build();

        elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of("device_info"));
    }

    /**
     * 移除指定设备信息中的分组
     */
    public void removeGroup(String deviceId, String groupId) {
        UpdateQuery updateQuery = UpdateQuery.builder(new CriteriaQuery(
                Criteria.where("deviceId").is(deviceId)))
                .withScript(String.format("ctx._source.group.remove('%s')", groupId))
                .withScriptType(ScriptType.INLINE)
                .build();

        elasticsearchRestTemplate.updateByQuery(updateQuery, IndexCoordinates.of("device_info"));
    }

    /**
     * 移除设备信息中的分组
     */
    public void removeGroup(String groupId) {
        UpdateQuery updateQuery = UpdateQuery.builder(new CriteriaQuery(new Criteria()
                .and("group." + groupId).exists()))
                .withScript(String.format("ctx._source.group.remove('%s')", groupId))
                .withScriptType(ScriptType.INLINE)
                .build();

        elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of("device_info"));
    }
}
