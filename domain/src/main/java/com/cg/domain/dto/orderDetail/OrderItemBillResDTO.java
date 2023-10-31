package com.cg.domain.dto.orderDetail;

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
public class OrderItemBillResDTO {
    private String productTitle;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal amount;

}
