package com.cg.domain.dto.tableOrder;

import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import com.cg.domain.enums.ETableStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.annotation.Annotation;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class TableOrderCreateReqDTO {
    private String title;
    private String zoneId;

    public TableOrder toTableOrder(Zone zone) {
        return new TableOrder()
                .setId(null)
                .setTitle(title)
                .setStatus(ETableStatus.EMPTY)
                .setZone(zone);
    }

    public TableOrder toTableOrder(Long tableOrderId, Zone zone) {
        return new TableOrder()
                .setId(tableOrderId)
                .setTitle(title)
                .setStatus(ETableStatus.EMPTY)
                .setZone(zone)
                ;
    }
}
