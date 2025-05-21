package com.temm.skillify.model.enums;

public enum SalesPlanMembershipType {
    ACTIVE("ACTIVE"),
    NOT_ACTIVE("NOT_ACTIVE");

    private final String role;

    SalesPlanMembershipType(String role) {
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