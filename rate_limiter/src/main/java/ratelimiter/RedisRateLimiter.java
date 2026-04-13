package ratelimiter;

import redis.clients.jedis.Jedis;

public class RedisRateLimiter implements RateLimiter {

    private final Jedis jedis;
    private final int requestThreshold = 60;
    private final long windowSize = 60 * 1000;

    public RedisRateLimiter() {
        jedis = new Jedis("localhost", 6379);
    }

    @Override
    public boolean addRequest(String ip) {
        long windowStart = (System.currentTimeMillis() / windowSize) * windowSize;
        String currentKey = "rate:" + ip + ":" + windowStart;
        String previousKey = "rate:" + ip + ":" + (windowStart - windowSize);

        long currentCount = jedis.incr(currentKey);
        jedis.expire(currentKey, 128);

        String prev = jedis.get(previousKey);
        long previousCount = prev == null ? 0 : Long.parseLong(prev);

        double weight = (double) (System.currentTimeMillis() - windowStart) / windowSize;
        double effectiveCount = (previousCount * (1 - weight)) + currentCount;

        return effectiveCount < requestThreshold;
    }
}
