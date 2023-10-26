package com.cg.domain.dto.orderDetail;

import com.cg.domain.enums.EOrderDetailStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class OrderDetailKitchenTableDTO {
    private Long orderDetailId;
    private String tableName;
    private Long productId;
    private String productTitle;
    private String note;
    private Long count;
    private Long quantity;
    private String unitTilte;

    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updateAt;

    public OrderDetailKitchenTableDTO(Long orderDetailId, String tableName, Long productId, String productTitle, String note, Long count, Long quantity, String unitTilte, EOrderDetailStatus status, Date updateAt) {
        this.orderDetailId = orderDetailId;
        this.tableName = tableName;
        this.productId = productId;
        this.productTitle = productTitle;
        this.note = note;
        this.count = count;
        this.quantity = quantity;
        this.unitTilte = unitTilte;
        this.status = String.valueOf(status);
        this.updateAt = updateAt;
    }
}
