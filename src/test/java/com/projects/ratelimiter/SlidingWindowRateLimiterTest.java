package com.projects.ratelimiter;

import com.projects.ratelimiter.RateLimiters.SlidingWindowRateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowRateLimiterTest {
    private SlidingWindowRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = SlidingWindowRateLimiter.getInstance(5, 1, 10);
    }

    @Test
    void testAllowRequestWithinLimit() {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest());
        }
        assertFalse(rateLimiter.allowRequest());
    }

    @Test
    void testAllowRequestAfterPartialWindowReset() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest());
        }
        assertFalse(rateLimiter.allowRequest());

        Thread.sleep(500); // Wait for half the window to reset

        assertTrue(rateLimiter.allowRequest());
        assertFalse(rateLimiter.allowRequest());
    }
}
