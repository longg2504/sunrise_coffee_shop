package com.cg.domain.entity;

import com.cg.domain.dto.unit.UnitDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="units")
@Accessors(chain = true)
public class Unit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "unit")
    @JsonIgnore
    private List<SupplierIngredient> supplierIngredients;

    @OneToMany(mappedBy = "unit")
    @JsonIgnore
    private List<Product> products;


    public UnitDTO toUnitDTO() {
        return new UnitDTO()
                .setId(id)
                .setTitle(title);
    }
}
