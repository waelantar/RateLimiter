package com.projects.ratelimiter;

import com.projects.ratelimiter.RateLimiters.SlidingWindowRateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowRateLimiterTest {
    private SlidingWindowRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        // Resetting state by creating a new instance for each test
        rateLimiter = SlidingWindowRateLimiter.getInstance(5, 1, 10);
    }

    @Test
    void testAllowRequestWithinLimit() {
        // Test that requests are allowed within the limit
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest(), "Request " + (i + 1) + " should be allowed");
        }
        // Test that the limit is enforced
        assertFalse(rateLimiter.allowRequest(), "Request 6 should be denied");
    }

    @Test
    void testAllowRequestAfterPartialWindowReset() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest(), "Request " + (i + 1) + " should be allowed");
        }
        assertFalse(rateLimiter.allowRequest(), "Request 6 should be denied");

        Thread.sleep(500); // Wait for half the window to pass

        // Some of the window should be reset now, so one request should be allowed
        assertTrue(rateLimiter.allowRequest(), "Request should be allowed after partial reset");
        assertFalse(rateLimiter.allowRequest(), "Request after partial reset should still enforce the limit");
    }

    @Test
    void testAllowRequestAfterFullWindowReset() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest(), "Request " + (i + 1) + " should be allowed");
        }
        assertFalse(rateLimiter.allowRequest(), "Request 6 should be denied");

        Thread.sleep(1000); // Wait for the full window to pass

        // The window should be fully reset, so requests should be allowed again
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.allowRequest(), "Request after full reset should be allowed");
        }
        assertFalse(rateLimiter.allowRequest(), "Request after full reset and 5 requests should be denied");
    }
}
