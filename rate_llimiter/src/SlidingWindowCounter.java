public class SlidingWindowCounter {
    private final long windowSize = 60 * 1000;
    private final int requestThreshold = 60;
    private long windowStart;
    private int windowRequests = 0;
    private int previousCount = 0;


    public SlidingWindowCounter() { windowStart = System.currentTimeMillis(); }


    public synchronized boolean addRequest() {
        long now  = System.currentTimeMillis();

        if (now > windowStart + windowSize) {
            previousCount = windowRequests;
            windowRequests = 0;
            windowStart = now;
        }

        double weight = (double) (now - windowStart) / windowSize;
        double effectiveCount = (previousCount * (1 - weight)) + windowRequests;

        if (effectiveCount < requestThreshold) {
            windowRequests++;
            return true;
        }
        return false;
    }
}
