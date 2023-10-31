package com.cg.domain.dto.orderDetail;

import com.cg.domain.enums.EOrderDetailStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderDetailKitchenWaiterDTO {
    private Long orderItemId;
    private Long tableId;
    private String tableName;
    private Long productId;
    private String productTitle;
    private String note;
    private Long quantity;
    private String unitTitle;
    private String status;
    private Date updatedAt;

    public OrderDetailKitchenWaiterDTO(Long orderItemId, Long tableId, String tableName, Long productId, String productTitle, String note, Long quantity, String unitTitle, EOrderDetailStatus status, Date updatedAt) {
        this.orderItemId = orderItemId;
        this.tableId = tableId;
        this.tableName = tableName;
        this.productId = productId;
        this.productTitle = productTitle;
        this.note = note;
        this.quantity = quantity;
        this.unitTitle = unitTitle;
        this.status = String.valueOf(status);
        this.updatedAt = updatedAt;
    }
}
