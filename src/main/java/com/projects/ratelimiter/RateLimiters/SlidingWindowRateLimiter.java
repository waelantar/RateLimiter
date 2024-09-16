package com.projects.ratelimiter.RateLimiters;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowRateLimiter implements RateLimiter {
    private final int maxRequests;
    private final long windowSizeInSeconds;
    private final AtomicInteger[] buckets;
    private final long[] bucketTimestamps;
    private final int bucketCount;

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeInSeconds, int bucketCount) {
        this.maxRequests = maxRequests;
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.bucketCount = bucketCount;
        this.buckets = new AtomicInteger[bucketCount];
        this.bucketTimestamps = new long[bucketCount];
        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new AtomicInteger(0);
            bucketTimestamps[i] = Instant.now().getEpochSecond();
        }
    }

    public synchronized static SlidingWindowRateLimiter getInstance(int maxRequests, long windowSizeInSeconds, int bucketCount) {
        return new SlidingWindowRateLimiter(maxRequests, windowSizeInSeconds, bucketCount);
    }

    @Override
    public synchronized boolean allowRequest() {
        long currentTimeSeconds = Instant.now().getEpochSecond();
        int currentBucket = (int) (currentTimeSeconds % bucketCount);
        resetOldBuckets(currentTimeSeconds);

        int totalRequests = 0;
        for (int i = 0; i < bucketCount; i++) {
            totalRequests += buckets[i].get();
        }

        if (totalRequests < maxRequests) {
            buckets[currentBucket].incrementAndGet();
            bucketTimestamps[currentBucket] = currentTimeSeconds; // Update bucket timestamp
            return true;
        }

        return false;
    }

    private void resetOldBuckets(long currentTimeSeconds) {
        long windowStart = currentTimeSeconds - windowSizeInSeconds;
        for (int i = 0; i < bucketCount; i++) {
            if (bucketTimestamps[i] < windowStart) {
                buckets[i].set(0); // Reset bucket if it's outside the window
                bucketTimestamps[i] = currentTimeSeconds; // Update timestamp
            }
        }
    }
}
