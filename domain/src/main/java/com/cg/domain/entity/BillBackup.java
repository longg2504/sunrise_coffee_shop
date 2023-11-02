package com.cg.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bill_backup")
@Accessors(chain = true)
public class BillBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unit;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal price;

    private int quantity;


    private Boolean paid;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal amount;

    private String note;
    private Long tableId;
    private Long productId;
    private String productTitle;
    private Long orderId;

    @ManyToOne
    @JoinColumn(name= "table_backup", nullable = false)
    private TableOrderBackup tableBackup;

}
