package com.summer.auth.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Renjun Yu
 * @date 2024/04/14 20:44
 */
@Component
public class PrometheusMetricsConfig implements MetricsTracker{
    private static final String ANDI_REQUEST_COUNT = "andi.request.count";
    @SuppressWarnings("FieldCanBeLocal")
    private final MeterRegistry registry;
    @SuppressWarnings("FieldCanBeLocal")
    private final Counter requestCounter;


    @Autowired
    public PrometheusMetricsConfig(final MeterRegistry registry) {
        this.registry = registry;
        this.requestCounter = Counter
                .builder(ANDI_REQUEST_COUNT)
                .description("接口请求次数")
                .tags("regiony", "andi-auth")
                .register(registry);
    }

    @Override
    public void recordRequest() {
        requestCounter.increment();
    }

//    @Override
//    public void close() {
//        meterRegistry.remove(requestCounter);
//    }
}
