package com.cg.domain.entity;

import com.cg.domain.dto.orderDetail.OrderDetailKitchenGroupDTO;
import com.cg.domain.dto.orderDetail.OrderDetailKitchenWaiterDTO;
import com.cg.domain.dto.orderDetail.OrderDetailResDTO;
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
                .setQuantity(quantity)
                .setUnitTitle(product.getUnit().getTitle())
                .setStatus(String.valueOf(status))
                .setUpdatedAt(getUpdatedAt())
                ;
    }

    public OrderDetailKitchenGroupDTO toOrderDetailKitchenGroupDTO() {
        return new OrderDetailKitchenGroupDTO()
                .setProductId(product.getId())
                .setProductTitle(product.getTitle())
                .setNote(note)
                .setQuantity(quantity)
                .setUnitTitle(product.getUnit().getTitle())
                .setStatus(String.valueOf(status))
                ;
    }

    public OrderDetailResDTO toOrderItemResDTO() {
        return new OrderDetailResDTO()
                .setId(id)
                .setUnitTitle(product.getUnit().getTitle())
                .setPrice(product.getPrice())
                .setQuantity(quantity)
                .setAmount(amount)
                .setStatus(String.valueOf(status))
                .setNote(note)
                .setProductId(product.getId())
                .setProductTitle(product.getTitle())
                ;
    }

    public OrderDetail initValue(OrderDetail newOrderItem) {
        return new OrderDetail()
                .setId(id)
                .setOrder(newOrderItem.getOrder())
                .setStatus(newOrderItem.getStatus())
                .setPrice(newOrderItem.getPrice())
                .setQuantity(newOrderItem.getQuantity())
                .setAmount(newOrderItem.getAmount())
                .setProduct(newOrderItem.getProduct())
                .setNote(newOrderItem.getNote())
                ;
    }

    public BillBackup toBillBackup(TableOrderBackup tableBackup){
        return new BillBackup()
                .setId(id)
                .setPrice(price)
                .setQuantity(Math.toIntExact(quantity))
                .setUnit(product.getUnit().getTitle())
                .setPaid(order.getPaid())
                .setAmount(amount)
                .setNote(note)
                .setTableId(order.getTableOrder().getId())
                .setProductId(product.getId())
                .setProductTitle(product.getTitle())
                .setOrderId(order.getId())
                .setTableBackup(tableBackup)
                ;
    }

    public OrderDetail backupValue(BillBackup item, Order order, TableOrder appTable, Product product) {
        return new OrderDetail()
                .setId(id)
                .setOrder(order)
                .setStatus(status)
                .setPrice(item.getPrice())
                .setQuantity((long) item.getQuantity())
                .setAmount(item.getAmount())
                .setProduct(product)
                .setNote(item.getNote())
                ;
    }






}
