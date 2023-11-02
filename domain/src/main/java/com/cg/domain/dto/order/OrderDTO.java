package com.cg.domain.dto.order;

import com.cg.domain.dto.orderDetail.OrderDetailDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.TableOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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
    private Date updatedAt;

    public OrderDTO(Long id,BigDecimal totalAmount, TableOrder tableOrder, Date updatedAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.tableOrder = tableOrder.toTableOrderDTO();
        this.updatedAt = updatedAt;

    }

    public Order toOrder() {
        return new Order()
                .setId(id)
                .setTotalAmount(totalAmount)
                .setTableOrder(tableOrder.toTableOrder())
                ;

    }
}
