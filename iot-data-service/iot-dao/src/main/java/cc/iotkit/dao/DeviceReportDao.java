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

import cc.iotkit.model.device.message.DeviceReport;
import cc.iotkit.model.stats.TimeData;
import lombok.SneakyThrows;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeviceReportDao {

    @Autowired
    private ElasticsearchRestTemplate template;

    /**
     * 按用户统计时间段内上报次数
     */
    public List<TimeData> getDeviceMessageStatsWithUid(String uid, long start, long end) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("time")
                        .from(start, true).to(end, true));
        if (uid != null) {
            queryBuilder =
                    queryBuilder.must(QueryBuilders.termQuery("uid", uid));
        }

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withAggregations(AggregationBuilders.dateHistogram("agg")
                        .field("time")
                        .calendarInterval(DateHistogramInterval.HOUR)
                        .calendarInterval(DateHistogramInterval.hours(1))
                )
                .build();

        ElasticsearchAggregations result = (ElasticsearchAggregations) template
                .search(query, DeviceReport.class).getAggregations();
        ParsedDateHistogram histogram = result.aggregations().get("agg");

        List<TimeData> data = new ArrayList<>();
        for (Histogram.Bucket bucket : histogram.getBuckets()) {
            long seconds = ((ZonedDateTime) bucket.getKey()).toInstant().getEpochSecond();
            data.add(new TimeData(seconds * 1000, bucket.getDocCount()));
        }

        return data;
    }

    /**
     * 统计时间段内上报次数
     */
    @SneakyThrows
    public List<TimeData> getDeviceMessageStats(long start, long end) {
        return getDeviceMessageStatsWithUid(null, start, end);
    }
}
