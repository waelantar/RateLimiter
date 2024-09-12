package com.projects.ratelimiter;

import com.projects.ratelimiter.RateLimiters.TokenBucketRateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenBucketRateLimiterTest {
    private TokenBucketRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = TokenBucketRateLimiter.getInstance(5, 1);
    }

    @Test
    void testAllowRequestWithinLimit() {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest());
        }
        assertFalse(rateLimiter.allowRequest());
    }

    @Test
    void testAllowRequestAfterRefill() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest());
        }
        assertFalse(rateLimiter.allowRequest());

        Thread.sleep(1000); // Wait for token refill

        assertTrue(rateLimiter.allowRequest());
    }
}
