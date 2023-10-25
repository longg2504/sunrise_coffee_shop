package com.cg.domain.dto.orderDetail;


import java.util.Date;

public interface IOrderDetailKitchenTableDTO {
    Long getOrderItemId();
    String getTableName();
    Long getProductId();
    String getProductTitle();
    String getNote();
    int getQuantity();
    String getUnitTitle();
    Date getUpdatedAt();
}
