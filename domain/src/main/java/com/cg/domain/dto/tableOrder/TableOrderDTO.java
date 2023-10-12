package com.cg.domain.dto.tableOrder;

import com.cg.domain.dto.zone.ZoneDTO;
import com.cg.domain.entity.TableOrder;
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
    private ZoneDTO zone;

    public TableOrderDTO(Long id, String title, ETableStatus status, Zone zone) {
        this.id = String.valueOf(id);
        this.title = title;
        this.status = status.getValue();
        this.zone = zone.toZoneDTO();
    }

    public TableOrder toTableOrder() {
        return new TableOrder()
                .setId(Long.valueOf(id))
                .setTitle(title)
                .setStatus(ETableStatus.valueOf(status))
                .setZone(zone.toZone());
    }
}
