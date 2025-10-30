package utils;

import java.util.*;
/**
 * Simple metrics collector counting named events and timing.
 */
public class SimpleMetrics implements Metrics {
    private long startTime = 0;
    private long endTime = 0;
    private final Map<String, Long> counters = new HashMap<>();

    @Override public void start() { startTime = System.nanoTime(); }
    @Override public void stop() { endTime = System.nanoTime(); }
    @Override public long elapsedNano() { return endTime - startTime; }
    @Override public void increment(String counter) { counters.put(counter, counters.getOrDefault(counter, 0L) + 1); }
    @Override public long getCount(String counter) { return counters.getOrDefault(counter, 0L); }
}
