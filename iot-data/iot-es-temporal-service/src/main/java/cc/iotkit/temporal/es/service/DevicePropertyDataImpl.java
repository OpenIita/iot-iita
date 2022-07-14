package cc.iotkit.temporal.es.service;

import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.temporal.IDevicePropertyData;
import cc.iotkit.temporal.es.document.DevicePropertyDoc;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DevicePropertyDataImpl implements IDevicePropertyData {

    @Autowired
    private ElasticsearchRestTemplate template;

    public List<DeviceProperty> findDevicePropertyHistory(String deviceId, String name, long start, long end) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("deviceId", deviceId))
                                .must(QueryBuilders.termQuery("name", name.toLowerCase()))
                                .must(QueryBuilders.rangeQuery("time")
                                        .from(start, true).to(end, true))
                )
                .withSorts(new FieldSortBuilder("time").order(SortOrder.ASC))
                .build();
        SearchHits<DevicePropertyDoc> result = template.search(query, DevicePropertyDoc.class);
        return result.getSearchHits().stream()
                .map(h -> h.getContent().de())
                .collect(Collectors.toList());
    }

    @Override
    public void addProperties(List<DeviceProperty> properties) {
        template.save(properties.stream().map(DevicePropertyDoc::new)
                .collect(Collectors.toList()));
    }


}
