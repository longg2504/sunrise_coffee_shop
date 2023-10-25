package com.cg.domain.dto.orderDetail;
import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.dto.tableOrder.TableOrderResDTO;
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
public class OrderDetailCreResDTO {
    private Long orderDetailId;
    private TableOrderResDTO table;
    private Long productId;
    private String title;
    private BigDecimal price;
    private Long quantity;
    private Long quantityDelivery;
    private BigDecimal amount;
    private String note;
    private BigDecimal totalAmount;
    private AvatarResDTO avatar;
    private String status;


}
