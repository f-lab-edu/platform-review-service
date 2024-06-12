package com.prs.ps.type;

public enum PlatformStatus {
    WAIT("WAIT"),
    ACCEPT("ACCEPT"),
    DENY("DENY");

    private final String status;

    PlatformStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
