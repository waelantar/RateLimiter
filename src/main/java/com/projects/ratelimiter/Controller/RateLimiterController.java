package com.projects.ratelimiter.Controller;

import com.projects.ratelimiter.Config.RateLimiterMetrics;
import com.projects.ratelimiter.Enum.RateLimiterType;
import com.projects.ratelimiter.Factory.RateLimiterFactory;
import com.projects.ratelimiter.RateLimiters.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class RateLimiterController {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterController.class);

    private final RateLimiter rateLimiter;
    private final RateLimiterMetrics metrics;

    @Autowired
    public RateLimiterController(RateLimiterFactory rateLimiterFactory, RateLimiterMetrics metrics) {
        this.rateLimiter = rateLimiterFactory.createRateLimiter(RateLimiterType.FIXED_WINDOW);
        this.metrics = metrics;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testRateLimiter() {
        logger.debug("Received request to /api/test");
        if (rateLimiter.allowRequest()) {
            logger.info("Request allowed by rate limiter");
            return ResponseEntity.ok("Request allowed");
        } else {
            logger.warn("Request rejected by rate limiter");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
    }
}
