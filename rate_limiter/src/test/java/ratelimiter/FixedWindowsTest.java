package ratelimiter;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FixedWindowsTest {

    @Test
    void allowsRequestsWithinThreshold() {
        FixedWindow limiter = new FixedWindow();

        for (int i = 0; i < 60; i++) {
            assertTrue(limiter.addRequest(), "request " + (i + 1) + " should be allowed");
        }
    }

    @Test
    void rejectsWhenThresholdIsHit() {
        FixedWindow limiter = new FixedWindow();

        for (int i = 0; i < 60; i++) {
            assertTrue(limiter.addRequest(), "request " + (i + 1) + " should be allowed");
        }

        assertFalse(limiter.addRequest(), "request 61 should be rejected");
    }

    @Test
    // Reflection is used here because FixedWindow hardcodes time and keeps its window state private.
    void allowsAgainAfterWindowResets() throws Exception {
        FixedWindow limiter = new FixedWindow();
        long windowSize = getLongField(limiter, "windowSize");

        setField(limiter, "windowRequests", 60);
        setField(limiter, "windowStart", System.currentTimeMillis() - windowSize);

        assertTrue(limiter.addRequest(), "a request should be allowed after the window resets");
    }

    private static long getLongField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getLong(target);
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
