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
@Setter
@Getter
@Accessors(chain = true)
public class TableOrderDTO {
    private String id;
    private String title;
    private String status;
    private String zone;

    public TableOrderDTO(Long id, String title, ETableStatus status, Zone zone) {
        this.id = String.valueOf(id);
        this.title = title;
        this.status = status.getValue();
        this.zone = zone.getTitle();
    }

    public TableOrderDTO toTableOrderDTO() {
        return new TableOrderDTO()
                .setId(id)
                .setTitle(title)
                .setStatus(status)
                .setZone(zone);
    }
}
