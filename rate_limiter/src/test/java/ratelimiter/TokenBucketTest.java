package ratelimiter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenBucketTest {

    @Test
    void shouldAllowRequestWhenBucketHasTokens() {
        TokenBucket bucket = new TokenBucket();
        assertTrue(bucket.addRequest());
    }

    @Test
    void rejectWhenBucketIsEmpty() {
        TokenBucket bucket = new TokenBucket();
        for (int i = 0; i < 10; i++) {
            bucket.addRequest();
        }
        assertFalse(bucket.addRequest());
    }

    @Test
    void allowAfterRefill() throws InterruptedException {
        TokenBucket bucket = new TokenBucket();
        for (int i = 0; i < 10; i++) {
            bucket.addRequest();
        }
        Thread.sleep(1100);
        assertTrue(bucket.addRequest());
    }
}
