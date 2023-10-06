package com.cg.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="invoices")
@Accessors(chain = true)
public class Invoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="staff_id" , referencedColumnName = "id" , nullable = false)
    private Staff staff;

    @Column(precision = 12, scale = 0 , nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    private List<InvoiceDetail> invoiceDetails;



}
