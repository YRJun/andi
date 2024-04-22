package com.summer.auth.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.summer.auth.config.MetricsTracker;
import com.summer.auth.dao.AndiDAO;
import com.summer.common.model.andi.AndiUser;
import com.summer.common.model.request.UserRequest;
import com.summer.common.model.response.AndiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/20 16:00
 */
@Service
@Slf4j
public class UserService {
    @Resource
    private AndiDAO andiDAO;
    @Resource
    MetricsTracker metricsTracker;
    @Resource
    ElasticsearchClient client;

    private record Zhexiantu(String name, String value){}
    public AndiResponse<?> queryUser(UserRequest request) {
        final AndiUser andiUser = andiDAO.queryUserByUsername(request.getUsername());
        metricsTracker.recordRequest();
        return AndiResponse.success(andiUser);
    }

    public AndiResponse<?> esTest() throws IOException {
        final List<Object> list = new ArrayList<>();
        SearchResponse<Object> search = client.search(s -> s
                        .index("ems-alarm-tncss")
                        .query(q -> q
                                .match(t -> t.field("alarm.alarm_id").query("40B1B1F33450492B4F5419C9B56E4A79")
                                ))
                , Object.class);
        log.info("elasticsearch8.11 search1 ok");
        for (Hit<Object> hit: search.hits().hits()) {
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
        list.add(new HashMap<>() {{put("docCountErrorUpperBound", docCountErrorUpperBound);put("sumOtherDocCount", sumOtherDocCount);}});
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
                                            .timeZone("Asia/Shanghai")
                                            .gte(JsonData.of(startTimeStr))
                                            .lte(JsonData.of(endTimeStr))))
                    , clazz);
        } catch (Exception e) {
            log.error("getAlarmFromEsBetweenCreateTime error,index[{}],timeField[{}],startTime[{}],endTime[{}]", index, timeField, startTime, endTime, e);
            return null;
        }
    }
}
