package com.cg.domain.dto.order;

import com.cg.domain.dto.orderDetail.OrderDetailCreResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderUpResDTO {
    private Long tableId;
    private BigDecimal totalAmount;

    private List<OrderDetailCreResDTO> orderDetails;
}
