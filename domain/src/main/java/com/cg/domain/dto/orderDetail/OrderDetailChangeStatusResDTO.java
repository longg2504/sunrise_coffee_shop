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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDetailChangeStatusResDTO {
    private Long id;
    private Long productId;
    private String title;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal amount;
    private String note;
    private String status;
    private AvatarResDTO avatar;

    public OrderDetailChangeStatusResDTO(Long id, Long productId, String title, BigDecimal price, Long quantity, BigDecimal amount, String note, EOrderDetailStatus status, Avatar avatar) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
        this.note = note;
        this.status = String.valueOf(status);
        this.avatar = avatar.toAvatarResDTO();
    }
}
