package com.cg.domain.dto.orderDetail;

import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.dto.product.ProductDTO;
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
public class OrderDetailResDTO {
    private Long id;
    private String unitTitle;
    private BigDecimal price;
    private Long quantity;
    private String status;
    private BigDecimal amount;
    private String note;
    private Long productId;
    private String productTitle;
    private AvatarResDTO productAvatar;

    public OrderDetailResDTO(Long id, String unitTitle, BigDecimal price, Long quantity, EOrderDetailStatus status, BigDecimal amount, String note, Long productId, String productTitle, Avatar productAvatar) {
        this.id = id;
        this.unitTitle = unitTitle;
        this.price = price;
        this.quantity = quantity;
        this.status = String.valueOf(status);
        this.amount = amount;
        this.note = note;
        this.productId = productId;
        this.productTitle = productTitle;
        this.productAvatar = productAvatar.toAvatarResDTO();
    }
}



