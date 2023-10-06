package com.cg.domain.entity;

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

}
