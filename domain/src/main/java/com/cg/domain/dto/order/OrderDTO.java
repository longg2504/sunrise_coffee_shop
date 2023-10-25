package com.cg.domain.dto.order;

import com.cg.domain.dto.orderDetail.OrderDetailDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
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
public class OrderDTO {
    private Long id;
    private StaffDTO staff;
    private TableOrderDTO tableOrder;
    private BigDecimal totalAmount;

    private OrderDetailDTO orderDetail;
    private Boolean paid;
}
