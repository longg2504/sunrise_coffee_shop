package com.cg.domain.enums;

public enum EOrderDetailStatus {
    NEW ("NEW"),
    WAITER("WAITER"),
    DOING("DOING"),
    DONE("DONE"),
    COOKING("COOKING");


    private final String value;

    EOrderDetailStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
