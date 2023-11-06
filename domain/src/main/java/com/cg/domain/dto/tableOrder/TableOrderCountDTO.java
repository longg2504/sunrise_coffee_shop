package com.cg.domain.dto.tableOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableOrderCountDTO {
    private Long countTableOrder;

    public TableOrderCountDTO(Long countTableOrder) {
        this.countTableOrder = countTableOrder;
    }
}
