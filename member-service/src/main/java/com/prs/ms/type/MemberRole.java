package com.prs.ms.type;

public enum MemberRole {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    MemberRole(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }

}
