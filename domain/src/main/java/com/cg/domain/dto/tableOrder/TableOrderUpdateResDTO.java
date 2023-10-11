package com.cg.domain.dto.tableOrder;

import com.cg.domain.enums.ETableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class TableOrderUpdateResDTO {
    private Long id;
    private String title;
    private ETableStatus status;
    private String zoneTitle;
}
