package com.projects.ratelimiter.RateLimiters;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FixedWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeInSeconds;
    private final AtomicInteger requestCount;
    private final AtomicReference<Instant> windowStart;

    private static FixedWindowRateLimiter instance;

    public FixedWindowRateLimiter(int maxRequests, long windowSizeInSeconds) {
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

        // Reset the window if the current time has passed the window duration
        if (now.isAfter(windowStartTime.plusSeconds(windowSizeInSeconds))) {
            windowStart.set(now);
            requestCount.set(0);
        }

        // Check if the current request count is within the allowed limit
        int currentCount = requestCount.get();
        if (currentCount < maxRequests) {
            requestCount.incrementAndGet();  // Increment the count after checking
            return true;
        }
        return false;  // Reject if the limit is reached
    }


}
