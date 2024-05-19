package com.summer.auth.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.JsonData;
import com.summer.auth.config.MetricsTracker;
import com.summer.auth.dao.AndiDAO;
import com.summer.common.model.andi.AndiUser;
import com.summer.common.model.response.AndiResponse;
import com.summer.common.util.OtherUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 16:00
 */
@Service
@Slf4j
public class TestService {
    @Resource
    private AndiDAO andiDAO;
    @Resource
    MetricsTracker metricsTracker;
    @Resource
    ElasticsearchClient client;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    public AndiResponse<?> snowflakeIdTest() {
        log.info("main{}-{}", Thread.currentThread().getName(), MDC.get("traceId"));
        int a = 1;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(a);
            System.out.println("baos.toByteArray().length:" + baos.toByteArray().length);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get object size", e);
        }
        return AndiResponse.success(OtherUtils.getSnowflakeId());
    }

    private record Zhexiantu(String name, String value) {
    }

    public AndiResponse<?> esTest() throws IOException {
        final List<Object> list = new ArrayList<>();
        SearchResponse<Object> search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                //.match(t -> t.field("alarm.alarm_id").query("40B1B1F33450492B4F5419C9B56E4A79"))
                                .multiMatch(t -> t.type(TextQueryType.BestFields).fields("alarm.alarm_id", "alarm.alarm_name").query("可能是name可能是id"))
                        )
                , Object.class);
        log.info("elasticsearch8.11 search1 ok");
        for (Hit<Object> hit : search.hits().hits()) {
            final Object source = hit.source();
            list.add(source);
        }
        search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .size(0)
                        .query(q -> q
                                .range(t -> t
                                        .field("alarm.ems_alarm_time")
                                        .timeZone("Asia/Shanghai")
                                        .gte(JsonData.of("2020-03-31T00:00:00.000Z"))
                                        .lte(JsonData.of("2024-04-23T00:00:00.000Z"))))
                        .aggregations(new HashMap<>() {{
                            put("ems_names", Aggregation.of(a -> a
                                    .terms(t -> t
                                            .field("alarm.ems_name.keyword")
                                            .size(10))));
                            put("total_docs_in_range", Aggregation.of(a -> a
                                    .valueCount(v -> v
                                            .field("_index"))));
                        }})
                , Object.class);
        log.info("elasticsearch8.11 search2 ok");
        final StringTermsAggregate emsNames = (StringTermsAggregate) search.aggregations().get("ems_names")._get();
        final Long docCountErrorUpperBound = emsNames.docCountErrorUpperBound();
        final Long sumOtherDocCount = emsNames.sumOtherDocCount();
        emsNames.buckets().array().forEach(b -> {
            list.add(new Zhexiantu(b.key()._get().toString(), String.valueOf(b.docCount())));
        });
        list.add(new HashMap<>() {{
            put("docCountErrorUpperBound", docCountErrorUpperBound);
            put("sumOtherDocCount", sumOtherDocCount);
        }});
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 24);
        calendar.set(Calendar.MILLISECOND, 4);
        final Date startTime = calendar.getTime();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 26);
        calendar.set(Calendar.MILLISECOND, 4);
        final Date endTime = calendar.getTime();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        final String startTimeStr = format.format(startTime);
        final String endTimeStr = format.format(endTime);
        search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                .range(r -> r
                                        .field("alarm.create_time")
                                        .timeZone("Asia/Shanghai")
                                        .gte(JsonData.of(startTimeStr))
                                        .lte(JsonData.of(endTimeStr))))
                , Object.class);
        System.out.println(search);
        search = this.getAlarmFromEsBetweenTime("ems-alarm-tncss", "alarm.create_time", startTime, endTime, Object.class);
        System.out.println(search);
        return AndiResponse.success(list);
    }

    public AndiResponse<?> esTest1() throws IOException {
        final List<Object> list = new ArrayList<>();
        SearchResponse<Object> search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                //.match(t -> t.field("alarm.alarm_id").query("40B1B1F33450492B4F5419C9B56E4A79"))
                                .multiMatch(t -> t.type(TextQueryType.BestFields).fields("alarm.alarm_id", "alarm.alarm_name").query("REPEAT_ALARM"))
                        )
                , Object.class);
        log.info(search.toString());
        search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                .match(t -> t.field("alarm.alarm_id").query("36FB1155E63FE39D1A02673F2713849A"))
                        )
                , Object.class);
        log.info(search.toString());
        final TotalHits total = search.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
        if (isExactResult) {
            log.info("There are " + total.value() + " results");
        } else {
            log.info("There are more than " + total.value() + " results");
        }
        search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                .wildcard(t -> t.field("alarm.alarm_id.keyword").value("*1155E63FE39D1A02673F2713849*"))
                        )
                , Object.class);
        log.info(search.toString());
        search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                .wildcard(t -> t.field("alarm.alarm_id.keyword").value("*1155E63FE39D1A02673F2713849A*"))
                        )
                , Object.class);
        log.info(search.toString());
        final Query wildcardQuery1 = WildcardQuery.of(t -> t
                .field("alarm.alarm_id.keyword")
                .value("*1155E63FE39D1A02673F2713849A*"))._toQuery();
        final Query wildcardQuery2 = WildcardQuery.of(t -> t
                .field("alarm.alarm_type.keyword")
                .value("REAT*"))._toQuery();
        search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                .bool(b -> b
                                        .must(wildcardQuery1, wildcardQuery2)
                                )
                        )
                , Object.class);
        log.info(search.toString());
        return AndiResponse.success(list);
    }

    public <T> SearchResponse<T> getAlarmFromEsBetweenTime(String index, String timeField, Date startTime, Date endTime, Class<T> clazz) {
        try {
            log.info("getAlarmFromEsBetweenTime start,index[{}],timeField[{}],startTime[{}],endTime[{}]", index, timeField, startTime, endTime);
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            final String startTimeStr = format.format(startTime);
            final String endTimeStr = format.format(endTime);
            return client.search(s -> s
                            .index(index)
                            .size(1)
                            .query(q -> q
                                    .range(r -> r
                                            .field(timeField)
                                            .timeZone("+08:00")
                                            //.timeZone("Asia/Shanghai")
                                            .gte(JsonData.of(startTimeStr))
                                            .lte(JsonData.of(endTimeStr))))
                    , clazz);
        } catch (Exception e) {
            log.error("getAlarmFromEsBetweenCreateTime error,index[{}],timeField[{}],startTime[{}],endTime[{}]", index, timeField, startTime, endTime, e);
            return null;
        }
    }
}
