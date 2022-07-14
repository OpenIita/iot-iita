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

import cc.iotkit.data.ICategoryData;
import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.data.IProductData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.stats.DataItem;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DeviceDao {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private IProductData productData;
    @Autowired
    private ICategoryData categoryData;

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
            products = productData.findByUid(uid);
        } else {
            products = productData.findAll();
        }
        Map<String, String> pkCateMap = new HashMap<>();
        for (Product product : products) {
            pkCateMap.put(product.getId(), product.getCategory());
        }

        //取品类列表
        Map<String, String> cateNames = new HashMap<>();
        for (Category category : categoryData.findAll()) {
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


}
