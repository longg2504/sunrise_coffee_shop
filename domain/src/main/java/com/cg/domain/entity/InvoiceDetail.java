package com.cg.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="invoice_detail")
public class InvoiceDetail extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 12, scale = 0 , nullable = false)
    private BigDecimal price;

    @Column
    private Long quantity;

    @Column(name="total_amount",precision = 12 , scale = 0 , nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "invoice_id" , referencedColumnName = "id" , nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "supplier_ingredient_id", referencedColumnName = "id" , nullable = false)
    private SupplierIngredient supplierIngredient;




}
