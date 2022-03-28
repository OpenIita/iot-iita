package cc.iotkit.dao;

import cc.iotkit.model.device.message.DeviceProperty;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DevicePropertyDao {

    @Autowired
    private ElasticsearchRestTemplate template;

    public List<DeviceProperty> findDevicePropertyHistory(String deviceId, String name, long start, long end) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("deviceId", deviceId))
                                .must(QueryBuilders.termQuery("name", name))
                                .must(QueryBuilders.rangeQuery("time")
                                        .from(start, true).to(end, true))
                )
                .withSorts(new FieldSortBuilder("time").order(SortOrder.ASC))
                .build();
        SearchHits<DeviceProperty> result = template.search(query, DeviceProperty.class);
        return result.getSearchHits().stream()
                .map(SearchHit::getContent).collect(Collectors.toList());
    }

}
