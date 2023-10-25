package com.cg.domain.dto.orderDetail;

import java.util.Date;

public interface IOrderDetailKitchenWaiterDTO {
    Long getOrderDetailId();
    Long getTableId();
    String getTableName();
    Long getProductId();
    String getProductTitle();
    String getNote();
    Long getQuantity();
    String getUnitTitle();
    Date getUpdatedAt();
    Boolean getCooking();

}
