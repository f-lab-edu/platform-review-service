package com.prs.ps.type;

public enum ActionStatus {


    CREATE("CREATE"),
    DELETE("DELETE"),
    UPDATE("UPDATE");

    private final String action;

    ActionStatus(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
