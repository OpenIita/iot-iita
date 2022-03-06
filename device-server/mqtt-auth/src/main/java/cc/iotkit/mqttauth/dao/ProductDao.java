package cc.iotkit.mqttauth.dao;

import cc.iotkit.model.product.Product;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class ProductDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public Product getProduct(String pk) {
        Query query = query(where("code").is(pk));
        return mongoTemplate.findOne(query, Product.class);
    }
}
