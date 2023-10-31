package com.cg.domain.dto.bill;

import com.cg.domain.dto.orderDetail.OrderItemBillResDTO;
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
public class BillPayResDTO {
    private Long orderId;

    private Long tableId;

    private BigDecimal totalAmount;

    List<OrderItemBillResDTO> items;

}
