package com.cg.domain.dto.order;

import com.cg.domain.dto.orderDetail.OrderDetailDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;

import java.math.BigDecimal;
import java.util.Date;

public interface IOrderDTO {
    Long getId();
    StaffDTO getStaff();
    TableOrderDTO getTable();
    Long getTableId();
    BigDecimal getTotalAmount();
    OrderDetailDTO getOrderDetail();
    Date getUpdatedAt();
    Boolean getPaid();

    String getTableName();

}
