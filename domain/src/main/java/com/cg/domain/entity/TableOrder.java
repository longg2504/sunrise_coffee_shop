package com.cg.domain.entity;

import com.cg.domain.dto.tableOrder.TableOrderCreateResDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.dto.tableOrder.TableOrderResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.cg.domain.enums.*;


import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="tables")
@Accessors(chain = true)
public class TableOrder extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private ETableStatus status;

    @ManyToOne
    @JoinColumn(name="zone_id" , referencedColumnName = "id" , nullable = false)
    private Zone zone;

    public TableOrderDTO toTableOrderDTO() {
        return new TableOrderDTO()
                .setId(String.valueOf(id))
                .setTitle(title)
                .setStatus(status.getValue())
                ;
    }
    public TableOrderCreateResDTO toTableOrderCreateResDTO() {
        return new TableOrderCreateResDTO()
                .setId(null)
                .setTitle(title)
                .setStatus(ETableStatus.EMPTY)
                .setZoneTitle(zone.getTitle());
    }

    public TableOrderResDTO toTableOrderResDTO() {
        return new TableOrderResDTO()
                .setId(id)
                .setTitle(title)
                .setStatus(status)
                .setZoneTitle(zone.getTitle())
                ;


    }

}
