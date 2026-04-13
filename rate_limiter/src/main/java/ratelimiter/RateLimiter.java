package ratelimiter;

public interface RateLimiter {
    boolean addRequest(String ip);

    default boolean addRequest() {
        return addRequest("");
    }
}
