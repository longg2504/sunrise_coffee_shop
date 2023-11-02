package com.cg.domain.enums;

import com.cg.exception.DataInputException;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ETableStatus {
    EMPTY("EMPTY"),
    BUSY("BUSY");

    private final String value;

    ETableStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    private static final Map<String, ETableStatus> NAME_MAP = Stream.of(values())
            .collect(Collectors.toMap(ETableStatus::toString, Function.identity()));


    public static ETableStatus fromString(final String name) {
        ETableStatus eTableStatus = NAME_MAP.get(name);
        if (null == eTableStatus) {
            throw new DataInputException(String.format("'%s' has no corresponding value. Accepted values: %s", name, Arrays.asList(values())));
        }
        return eTableStatus;
    }

}
