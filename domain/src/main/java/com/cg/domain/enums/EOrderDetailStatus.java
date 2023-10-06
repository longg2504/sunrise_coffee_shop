package com.cg.domain.enums;

public enum EOrderDetailStatus {
    NEW ("NEW"),
    DOING("DOING"),
    DONE("DONE");


    private final String value;

    EOrderDetailStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
