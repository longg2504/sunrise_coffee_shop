package com.cg.domain.dto.orderDetail;

import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.enums.EOrderDetailStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderDetailProductUpResDTO {
    private Long id;
    private Long productId;
    private String title;
    private BigDecimal price;
    private Long quantity;
    private Long quantityDelivery;
    private BigDecimal amount;
    private String note;
    private String status;
    private AvatarResDTO avatar;

    public OrderDetailProductUpResDTO(Long id, Long productId, String title, BigDecimal price, Long quantity,BigDecimal amount, String note, Avatar avatar) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
        this.note = note;
        this.avatar = avatar.toAvatarResDTO();
    }

    public OrderDetailProductUpResDTO(Long id, Long productId, String title, BigDecimal price, Long quantity,Long quantityDelivery ,BigDecimal amount, String note, EOrderDetailStatus status, Avatar avatar) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.quantityDelivery = quantityDelivery;
        this.amount = amount;
        this.note = note;
        this.status = String.valueOf(status);
        this.avatar = avatar.toAvatarResDTO();
    }
}
