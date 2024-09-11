package com.projects.ratelimiter.RateLimiters;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class TokenBucketRateLimiter implements RateLimiter {
    private final long capacity;
    private final double refillRate;
    private final AtomicLong availableTokens;
    private long lastRefillTimestamp;

    private static TokenBucketRateLimiter instance;

    private TokenBucketRateLimiter(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.availableTokens = new AtomicLong(capacity);
        this.lastRefillTimestamp = Instant.now().toEpochMilli();
    }

    public static synchronized TokenBucketRateLimiter getInstance(long capacity, double refillRate) {
        if (instance == null) {
            instance = new TokenBucketRateLimiter(capacity, refillRate);
        }
        return instance;
    }

    @Override
    public synchronized boolean allowRequest() {
        refill();
        if (availableTokens.get() > 0) {
            availableTokens.decrementAndGet();
            return true;
        }
        return false;
    }

    private void refill() {
        long now = Instant.now().toEpochMilli();
        long timePassed = now - lastRefillTimestamp;
        long tokensToAdd = (long) (timePassed * refillRate / 1000);

        if (tokensToAdd > 0) {
            long newTokens = Math.min(capacity, availableTokens.get() + tokensToAdd);
            availableTokens.set(newTokens);
            lastRefillTimestamp = now;
        }
    }
}

