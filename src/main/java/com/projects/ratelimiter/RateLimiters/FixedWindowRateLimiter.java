package com.projects.ratelimiter.RateLimiters;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FixedWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeInSeconds;
    private final AtomicInteger requestCount;
    private final AtomicReference<Instant> windowStart;

    private static FixedWindowRateLimiter instance;

    private FixedWindowRateLimiter(int maxRequests, long windowSizeInSeconds) {
        this.maxRequests = maxRequests;
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.requestCount = new AtomicInteger(0);
        this.windowStart = new AtomicReference<>(Instant.now());
    }

    public static synchronized FixedWindowRateLimiter getInstance(int maxRequests, long windowSizeInSeconds) {
        if (instance == null) {
            instance = new FixedWindowRateLimiter(maxRequests, windowSizeInSeconds);
        }
        return instance;
    }

    @Override
    public boolean allowRequest() {
        Instant now = Instant.now();
        Instant windowStartTime = windowStart.get();

        if (now.isAfter(windowStartTime.plusSeconds(windowSizeInSeconds))) {
            windowStart.set(now);
            requestCount.set(0);
        }

        return requestCount.incrementAndGet() <= maxRequests;
    }
}
