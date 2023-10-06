package com.cg.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="supplier_ingredient")
@Accessors(chain = true)
public class SupplierIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id" , referencedColumnName = "id" , nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "ingredient_id" , referencedColumnName = "id" , nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name= "unit_id" , referencedColumnName = "id" , nullable = false)
    private Unit unit;

    @OneToMany(mappedBy = "supplierIngredient")
    @JsonIgnore
    private List<InvoiceDetail> invoiceDetails;
}
