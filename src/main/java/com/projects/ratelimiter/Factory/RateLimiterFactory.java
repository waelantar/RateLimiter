package com.projects.ratelimiter.Factory;

import com.projects.ratelimiter.Enum.RateLimiterType;
import com.projects.ratelimiter.RateLimiters.FixedWindowRateLimiter;
import com.projects.ratelimiter.RateLimiters.RateLimiter;
import com.projects.ratelimiter.RateLimiters.SlidingWindowRateLimiter;
import com.projects.ratelimiter.RateLimiters.TokenBucketRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterFactory {
    @Value("${rate-limiter.max-requests}")
    private int maxRequests;

    @Value("${rate-limiter.window-size-seconds}")
    private long windowSizeSeconds;

    @Value("${rate-limiter.bucket-count}")
    private int bucketCount;

    @Value("${rate-limiter.token-bucket.capacity}")
    private long tokenBucketCapacity;

    @Value("${rate-limiter.token-bucket.refill-rate}")
    private double tokenBucketRefillRate;

    public RateLimiter createRateLimiter(RateLimiterType type) {
        switch (type) {
            case FIXED_WINDOW:
                return FixedWindowRateLimiter.getInstance(maxRequests, windowSizeSeconds);
            case SLIDING_WINDOW:
                return SlidingWindowRateLimiter.getInstance(maxRequests, windowSizeSeconds, bucketCount);
            case TOKEN_BUCKET:
                return TokenBucketRateLimiter.getInstance(tokenBucketCapacity, tokenBucketRefillRate);
            default:
                throw new IllegalArgumentException("Unknown rate limiter type: " + type);
        }
    }
}
