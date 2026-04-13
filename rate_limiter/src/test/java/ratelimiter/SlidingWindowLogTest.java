package ratelimiter;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayDeque;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlidingWindowLogTest {

    @Test
    void allowsRequestsWithinThreshold() {
        SlidingWindowLog limiter = new SlidingWindowLog();

        for (int i = 0; i < 60; i++) {
            assertTrue(limiter.addRequest(), "request " + (i + 1) + " should be allowed");
        }
    }

    @Test
    void rejectsWhenThresholdIsHit() {
        SlidingWindowLog limiter = new SlidingWindowLog();

        for (int i = 0; i < 60; i++) {
            assertTrue(limiter.addRequest(), "request " + (i + 1) + " should be allowed");
        }

        assertFalse(limiter.addRequest(), "request 61 should be rejected");
    }

    @Test
    // Reflection is used here because SlidingWindowLog hardcodes time and keeps its request log private.
    void allowsAgainAfterWindowResets() throws Exception {
        SlidingWindowLog limiter = new SlidingWindowLog();
        long windowSize = getLongField(limiter, "windowSize");
        long now = System.currentTimeMillis();

        ArrayDeque<Long> requestLog = getRequestLog(limiter);
        for (int i = 0; i < 60; i++) {
            requestLog.addLast(now - windowSize - 1);
        }

        assertTrue(limiter.addRequest(), "a request should be allowed after old requests expire");
    }

    @SuppressWarnings("unchecked")
    private static ArrayDeque<Long> getRequestLog(SlidingWindowLog limiter) throws Exception {
        Field field = SlidingWindowLog.class.getDeclaredField("requestLog");
        field.setAccessible(true);
        return (ArrayDeque<Long>) field.get(limiter);
    }

    private static long getLongField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getLong(target);
    }
}
