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

    private Long orderCurrentId;

    private Long tableTargetId;

    private Long orderTargetId;

    public TableOrderBackup(Long id, Long tableCurrentId, Long orderCurrentId, Long tableTargetId, Long orderTargetId) {
        this.id = id;
        this.tableCurrentId = tableCurrentId;
        this.orderCurrentId = orderCurrentId;
        this.tableTargetId = tableTargetId;
        this.orderTargetId = orderTargetId;
    }
}
