package com.cg.domain.dto.tableOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableOrderWithZoneCountDTO {
    private Long countTableOrder;
    private String zoneName;

    public TableOrderWithZoneCountDTO(Long countTableOrder, String zoneName) {
        this.countTableOrder = countTableOrder;
        this.zoneName = zoneName;
    }
}
