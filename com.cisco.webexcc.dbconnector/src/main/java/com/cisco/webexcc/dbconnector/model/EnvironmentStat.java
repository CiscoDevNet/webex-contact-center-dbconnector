package com.cisco.webexcc.dbconnector.model;

public class EnvironmentStat {
    private long totalHits;
    private long failedHits;

    public EnvironmentStat(long totalHits, long failedHits) {
        this.totalHits = totalHits;
        this.failedHits = failedHits;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public long getFailedHits() {
        return failedHits;
    }

    public void setFailedHits(long failedHits) {
        this.failedHits = failedHits;
    }
}
