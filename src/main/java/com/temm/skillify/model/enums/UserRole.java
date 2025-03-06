package com.temm.skillify.model.enums;


public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    ESTUDANTE("ROLE_ESTUDANTE"),
    MENTOR("ROLE_MENTOR");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }
}