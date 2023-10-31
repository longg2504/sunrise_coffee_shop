package com.cg.domain.entity;

import com.cg.domain.dto.orderDetail.OrderDetailKitchenWaiterDTO;
import com.cg.domain.enums.EOrderDetailStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_detail")
@Accessors(chain = true)
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

    @Column(scale = 0)
    private Long quantityDelivery;

    @Column(precision = 12 , scale = 0 , nullable = false)
    private BigDecimal price;

    @Column(precision = 12 , scale = 0 , nullable = false)
    private BigDecimal amount;

    private String note;

    @Enumerated(EnumType.STRING)
    private EOrderDetailStatus status;

    public OrderDetailKitchenWaiterDTO toOrderItemKitchenWaiterDTO() {
        return new OrderDetailKitchenWaiterDTO()
                .setOrderItemId(id)
                .setTableId(order.getTableOrder().getId())
                .setTableName(order.getTableOrder().getTitle())
                .setProductId(product.getId())
                .setProductTitle(product.getTitle())
                .setNote(note)
                .setQuantity((long) quantity)
                .setUnitTitle(product.getUnit().getTitle())
                .setStatus(String.valueOf(status))
                .setUpdatedAt(getUpdatedAt())
                ;
    }
}
