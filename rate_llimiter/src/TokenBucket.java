public class TokenBucket {
    private double tokens;
    private long lastRefillTime;
    private final double capacity = 10;
    private final double refillRate = 1.0;

    public TokenBucket() {
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean tryConsume() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double secondsElapsed = (now - lastRefillTime) / 1000.0;
        tokens = Math.min(capacity, tokens + secondsElapsed * refillRate);
        lastRefillTime = now;
    }

}
