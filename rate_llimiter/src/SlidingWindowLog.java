import java.util.ArrayDeque;

public class SlidingWindowLog implements RateLimiter{
    private int requestThreshold = 60;
    private final long windowSize = 60 * 1000;
    private final ArrayDeque<Long> requestLog = new ArrayDeque<>();

    public synchronized boolean addRequest() {
        long now = System.currentTimeMillis();

        while (!requestLog.isEmpty() && requestLog.peek() <= now - windowSize) {
            requestLog.poll();
        }

        if (requestLog.size() < requestThreshold) {
            requestLog.addLast(now);
            return true;
        }

        return false;
    }

}
