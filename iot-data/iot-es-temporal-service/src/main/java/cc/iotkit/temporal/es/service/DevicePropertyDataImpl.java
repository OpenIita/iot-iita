/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.temporal.es.service;

import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.temporal.IDevicePropertyData;
import cc.iotkit.temporal.es.document.DevicePropertyMapper;
import cc.iotkit.temporal.es.document.DocDeviceProperty;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        SearchHits<DocDeviceProperty> result = template.search(query, DocDeviceProperty.class);
        return result.getSearchHits().stream()
                .map(h -> DevicePropertyMapper.M.toDto(h.getContent()))
                .collect(Collectors.toList());
    }

    @Override
    public void addProperties(String deviceId, Map<String, Object> properties, long time) {
        List<DocDeviceProperty> deviceProperties = new ArrayList<>();
        properties.forEach((key, val) -> deviceProperties.add(
                new DocDeviceProperty(UUID.randomUUID().toString(), deviceId, key, val, time)
        ));

        template.save(deviceProperties);
    }


}
