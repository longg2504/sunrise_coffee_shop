package com.cg.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "table_backup")
@Accessors(chain = true)
public class TableOrderBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableCurrentId;
    private Long tableTargetId;
    private Long orderTargetId;
    private Boolean paid;

    public TableOrderBackup(Long id, Long tableCurrentId, Long tableTargetId, Long orderTargetId,Boolean paid) {
        this.id = id;
        this.tableCurrentId = tableCurrentId;
        this.tableTargetId = tableTargetId;
        this.orderTargetId = orderTargetId;
        this.paid = paid;
    }
}
