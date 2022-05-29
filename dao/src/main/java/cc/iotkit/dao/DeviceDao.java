package cc.iotkit.dao;

import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.stats.DataItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DeviceDao {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Paging<DeviceInfo> find(Criteria condition, int size, int page) {
        Query query = Query.query(condition);
        return new Paging<>(
                mongoTemplate.count(query, DeviceInfo.class),
                mongoTemplate.find(
                        query.with(PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createAt"))))
                        , DeviceInfo.class)
        );
    }

    /**
     * 更新设备属性
     */
    public void updateProperties(String deviceId, Map<String, Object> properties) {
        if (properties == null) {
            return;
        }
        Query query = Query.query(new Criteria().and("deviceId").is(deviceId));
        Update update = new Update();
        for (String key : properties.keySet()) {
            update.set("property." + key, properties.get(key));
        }
        mongoTemplate.updateFirst(query, update, DeviceInfo.class);
    }

    /**
     * 更新设备标签
     */
    public void updateTag(String deviceId, DeviceInfo.Tag tag) {
        Query query = Query.query(new Criteria().and("deviceId").is(deviceId));
        Update update = new Update();
        update.set("tag." + tag.getId(), tag);
        mongoTemplate.updateFirst(query, update, DeviceInfo.class);
    }

    /**
     * 设置设备标签值为空
     */
    public void setTagNull(String deviceId, String tagId) {
        Query query = Query.query(new Criteria().and("deviceId").is(deviceId));
        Update update = new Update();
        update.set("tag." + tagId, null);
        mongoTemplate.updateFirst(query, update, DeviceInfo.class);
    }

    /**
     * 获取按品类统计的用户设备数
     */
    public List<DataItem> getDeviceStatsByCategory(String uid) {
        MatchOperation matchOperation;
        if (StringUtils.isBlank(uid)) {
            matchOperation = Aggregation.match(new Criteria());
        } else {
            matchOperation = Aggregation.match(Criteria.where("uid").is(uid));
        }

        //先按产品分组统计
        GroupOperation groupOperation = Aggregation.group("productKey").count().as("total");
        ProjectionOperation projectionOperation = Aggregation.project("productKey", "uid");
        Aggregation aggregation = Aggregation.newAggregation(projectionOperation, groupOperation, matchOperation);
        AggregationResults<Map> result = mongoTemplate.aggregate(aggregation, DeviceInfo.class, Map.class);
        List<Map> stats = result.getMappedResults();

        //取用户产品列表
        List<Product> products;
        if (StringUtils.isBlank(uid)) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByUid(uid);
        }
        Map<String, String> pkCateMap = new HashMap<>();
        for (Product product : products) {
            pkCateMap.put(product.getId(), product.getCategory());
        }

        //取品类
        List<Category> categories = categoryRepository.findAll();
        Map<String, String> cateNames = new HashMap<>();
        for (Category category : categories) {
            cateNames.put(category.getId(), category.getName());
        }

        Map<String, Long> cateStats = new HashMap<>();
        for (Map stat : stats) {
            String productKey = stat.get("_id").toString();
            String cateName = cateNames.get(pkCateMap.get(productKey));
            //按品类汇总
            long total = cateStats.getOrDefault(cateName, 0L);
            total += (Integer) stat.get("total");
            cateStats.put(cateName, total);
        }

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
