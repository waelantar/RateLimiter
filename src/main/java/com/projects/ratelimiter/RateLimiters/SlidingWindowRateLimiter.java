package com.projects.ratelimiter.RateLimiters;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeInSeconds;
    private final AtomicInteger[] buckets;
    private final int bucketCount;
    private static SlidingWindowRateLimiter instance;

    private SlidingWindowRateLimiter(int maxRequests, long windowSizeInSeconds, int bucketCount) {
        this.maxRequests = maxRequests;
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.bucketCount = bucketCount;
        this.buckets = new AtomicInteger[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new AtomicInteger(0);
        }
    }

    public static synchronized SlidingWindowRateLimiter getInstance(int maxRequests, long windowSizeInSeconds, int bucketCount) {
        if (instance == null) {
            instance = new SlidingWindowRateLimiter(maxRequests, windowSizeInSeconds, bucketCount);
        }
        return instance;
    }

    @Override
    public boolean allowRequest() {
        long currentTimeSeconds = Instant.now().getEpochSecond();
        int currentBucket = (int) (currentTimeSeconds % bucketCount);
        long windowStart = currentTimeSeconds - windowSizeInSeconds;

        int totalRequests = 0;
        for (int i = 0; i < bucketCount; i++) {
            if ((currentTimeSeconds - i) >= windowStart) {
                totalRequests += buckets[i].get();
            } else {
                buckets[i].set(0);
            }
        }

        if (totalRequests < maxRequests) {
            buckets[currentBucket].incrementAndGet();
            return true;
        }

        return false;
    }
}
