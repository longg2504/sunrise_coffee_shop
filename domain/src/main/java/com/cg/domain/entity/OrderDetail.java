package com.cg.domain.entity;

import com.cg.domain.enums.EOrderDetailStatus;
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
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="order_id" , referencedColumnName = "id" , nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_id" , referencedColumnName = "id" , nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    @Column(precision = 12 , scale = 0 , nullable = false)
    private BigDecimal price;

    @Column(precision = 12 , scale = 0 , nullable = false)
    private BigDecimal amount;

    private String note;

    @Enumerated(EnumType.STRING)
    private EOrderDetailStatus status;
}
