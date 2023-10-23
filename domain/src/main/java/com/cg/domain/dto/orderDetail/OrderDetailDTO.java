package com.cg.domain.dto.orderDetail;

import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.enums.EOrderDetailStatus;

import java.math.BigDecimal;

public class OrderDetailDTO {
    private Long orderDetailId;
    private ProductDTO product;
    private Long quantity;
    private Long quantityDelivery;
    private BigDecimal price;
    private BigDecimal amount;
    private String note;
    private String status;


    public OrderDetailDTO(Long orderDetailId, ProductDTO product, Long quantity, Long quantityDelivery, BigDecimal price, BigDecimal amount, String note, EOrderDetailStatus status) {
        this.orderDetailId = orderDetailId;
        this.product = product;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.price = price;
        this.amount = amount;
        this.note = note;
        this.status = String.valueOf(status);
    }
}
