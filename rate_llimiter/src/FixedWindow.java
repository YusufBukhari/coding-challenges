public class FixedWindow {
    private final long windowSize = 60 * 1000;
    private final int requestThreshold = 60;
    private long windowStart;
    private int windowRequests = 0;

    public FixedWindow() {
        windowStart = (System.currentTimeMillis() / windowSize) * windowSize;
    }

    public synchronized boolean addRequest() {
        long now = (System.currentTimeMillis() / windowSize) * windowSize;

        if (now > windowStart) {
            windowStart = now;
            windowRequests = 0;
        }

        if (windowRequests < requestThreshold) {
            windowRequests++;
            return true;
        }
        return false;
    }
}
