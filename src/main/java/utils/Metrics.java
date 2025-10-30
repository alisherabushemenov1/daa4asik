package utils;

public interface Metrics {
    void start();
    void stop();
    long elapsedNano();
    void increment(String counter);
    long getCount(String counter);
}
