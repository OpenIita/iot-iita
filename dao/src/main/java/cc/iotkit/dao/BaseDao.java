package cc.iotkit.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class BaseDao<T> {

    protected MongoTemplate mongoTemplate;

    private Class<T> cls;

    public BaseDao(MongoTemplate mongoTemplate, Class<T> cls) {
        this.mongoTemplate = mongoTemplate;
        this.cls = cls;
    }

    public List<T> find(Criteria condition) {
        Query query = new Query();
        query.addCriteria(condition);
        return mongoTemplate.find(query, cls);
    }

    public List<T> find(Criteria condition, long skip, int count, Sort.Order order) {
        Query query = new Query();
        query.addCriteria(condition)
                .with(Sort.by(order))
                .skip(skip)
                .limit(count);
        return mongoTemplate.find(query, cls);
    }

    public long count(Criteria condition) {
        Query query = new Query();
        query.addCriteria(condition);
        return mongoTemplate.count(query, cls);
    }

    public <T> T save(String id, T entity) {
        if (id == null) {
            return mongoTemplate.save(entity);
        } else {
            mongoTemplate.updateFirst(query(where("_id").is(id)),
                    DaoTool.update(entity), entity.getClass());
            return (T) mongoTemplate.findById(id, entity.getClass());
        }
    }

    public <T> T save(T entity) {
        return mongoTemplate.save(entity);
    }

}
