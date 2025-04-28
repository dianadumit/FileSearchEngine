package org.searchengine.report;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ReportData {
    private final List<String> indexedPaths;
    private final List<String> skippedPaths;
    private final List<String> failedPaths;
    private final Instant startTime;
    private final Instant endTime;

    public ReportData(List<String> indexedPaths, List<String> skippedPaths, List<String> failedPaths, Instant startTime, Instant endTime) {
        this.indexedPaths = indexedPaths;
        this.skippedPaths = skippedPaths;
        this.failedPaths  = failedPaths;
        this.startTime    = startTime;
        this.endTime      = endTime;
    }

    public List<String> getIndexedPaths() {
        return indexedPaths;
    }

    public List<String> getSkippedPaths() {
        return skippedPaths;
    }

    public List<String> getFailedPaths() {
        return failedPaths;
    }

    public Instant getStartTime()  {
        return startTime; }
    public Instant getEndTime()    {
        return endTime; }
    public Duration getDuration()  {
        return Duration.between(startTime, endTime); }
}
