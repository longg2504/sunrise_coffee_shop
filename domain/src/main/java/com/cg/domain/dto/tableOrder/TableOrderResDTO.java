package com.cg.domain.dto.tableOrder;

import com.cg.domain.entity.Zone;
import com.cg.domain.enums.ETableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class TableOrderResDTO {
    private Long id;
    private String title;
    private ETableStatus status;
    private Zone zone;
}
