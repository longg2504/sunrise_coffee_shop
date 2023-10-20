package com.cg.domain.dto.orderDetail;

import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.entity.Unit;
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
public class OrderDetailByTableResDTO {
    private Long orderDetailId;
    private Long productId;
    private String title;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal amount;
    private String unit;
    private String note;
    private String status;
    private AvatarResDTO avatar;

//    public OrderDetailByTableResDTO(Long orderDetailId, Long productId, String title, BigDecimal price, Long quantity, BigDecimal amount, String unit, String note, String status, Avatar avatar) {
//        this.orderDetailId = orderDetailId;
//        this.productId = productId;
//        this.title = title;
//        this.price = price;
//        this.quantity = quantity;
//        this.amount = amount;
//        this.unit = unit;
//        this.note = note;
//        this.status = status;
//        this.avatar = avatar.toAvatarResDTO();
//    }

    public OrderDetailByTableResDTO(Long orderDetailId, Long productId, String title, BigDecimal price, Long quantity, BigDecimal amount, Unit unit, String note, EOrderDetailStatus status, Avatar avatar) {
        this.orderDetailId = orderDetailId;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.amount = amount;
        this.unit = String.valueOf(unit);
        this.note = note;
        this.status = String.valueOf(status);
        this.avatar = avatar.toAvatarResDTO();
    }
}
