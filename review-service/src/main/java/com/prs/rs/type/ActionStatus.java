package com.prs.rs.type;

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
