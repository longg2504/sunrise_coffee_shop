package com.cg.domain.dto.orderDetail;

public interface IOrderDetailKitchenGroupDTO {

    Long getOrderDetailId();
    Long getProductId();
    String getProductTitle();
    String getNote();

    Long getQuantity();

    String getUnitTitle();

    String getStatus();

    String getTableName();

}
