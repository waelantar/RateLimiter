package com.projects.ratelimiter;

import com.projects.ratelimiter.RateLimiters.FixedWindowRateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FixedWindowRateLimiterTest {
    private FixedWindowRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = FixedWindowRateLimiter.getInstance(5, 1);
    }

    @Test
    void testAllowRequestWithinLimit() {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest(), "Request " + (i + 1) + " should be allowed");
        }
        assertFalse(rateLimiter.allowRequest(), "Request 6 should be denied");
    }
    @BeforeEach
    void resetRateLimiter() {
        // Reset the singleton instance before each test
        FixedWindowRateLimiter.getInstance(5, 1);
    }

    @Test
    void testAllowRequestAfterRefill() throws InterruptedException {
        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(5, 1);  // Create new instance

        // Test first 5 requests within limit
        for (int i = 0; i < 20000; i++) {
            assertTrue(rateLimiter.allowRequest(), "Request " + (i + 1) + " should be allowed");
        }

        // 6th request should be blocked
        assertFalse(rateLimiter.allowRequest(), "Request 6 should be blocked");

        Thread.sleep(1000); // Wait for token refill (window reset)

        // After window resets, the next request should be allowed
        assertTrue(rateLimiter.allowRequest(), "Request after refill should be allowed");
    }

}

