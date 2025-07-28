package org.example.elprisappbackend.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class PerformanceConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "elpris-app-backend");
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(false); // Don't log payload for performance
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }

    @Bean
    public Timer externalApiTimer(MeterRegistry meterRegistry) {
        return Timer.builder("external.api.calls")
                .description("Timer for external API calls")
                .register(meterRegistry);
    }

    @Bean
    public Timer cacheOperationTimer(MeterRegistry meterRegistry) {
        return Timer.builder("cache.operations")
                .description("Timer for cache operations")
                .register(meterRegistry);
    }
}