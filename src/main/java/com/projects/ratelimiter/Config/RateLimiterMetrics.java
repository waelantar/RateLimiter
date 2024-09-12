package com.projects.ratelimiter.Config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterMetrics {

    private final Counter allowedRequests;
    private final Counter rejectedRequests;

    public RateLimiterMetrics(MeterRegistry registry) {
        this.allowedRequests = Counter.builder("rate_limiter.requests.allowed")
                .description("Number of requests allowed by the rate limiter")
                .register(registry);
        this.rejectedRequests = Counter.builder("rate_limiter.requests.rejected")
                .description("Number of requests rejected by the rate limiter")
                .register(registry);
    }

    public void incrementAllowedRequests() {
        allowedRequests.increment();
    }

    public void incrementRejectedRequests() {
        rejectedRequests.increment();
    }
}
