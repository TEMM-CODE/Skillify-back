package com.temm.skillify.model.enums;

public enum QuestionSuperAdminType {
    MATEMATICA("MATEMATICA"),
    FISICA("FISICA"),
    GEOGRAFIA("GEOGRAFIA"),
    BIOLOGIA("BIOLOGIA"),
    ENEM("ENEM"),
    FUVEST("FUVEST");

    private final String role;

    QuestionSuperAdminType(String role) {
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
