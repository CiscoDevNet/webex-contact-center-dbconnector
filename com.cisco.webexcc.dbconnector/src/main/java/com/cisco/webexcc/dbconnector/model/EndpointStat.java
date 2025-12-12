package com.cisco.webexcc.dbconnector.model;

import java.time.LocalDateTime;

public class EndpointStat {
    private long count;
    private long failedCount;
    private LocalDateTime lastExecution;

    public EndpointStat(long count, long failedCount, LocalDateTime lastExecution) {
        this.count = count;
        this.failedCount = failedCount;
        this.lastExecution = lastExecution;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(long failedCount) {
        this.failedCount = failedCount;
    }

    public LocalDateTime getLastExecution() {
        return lastExecution;
    }

    public void setLastExecution(LocalDateTime lastExecution) {
        this.lastExecution = lastExecution;
    }
}
