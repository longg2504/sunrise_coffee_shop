package com.cg.domain.enums;

public enum ERole {
    ROLE_ADMIN("ADMIN"),
    ROLE_CASHIER("CASHIER"),
    ROLE_BARISTA("BARISTA"),
    ROLE_MANAGER("MANAGER"),
    ROLE_STAFF_ORDER ("STAFF_ORDER");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
