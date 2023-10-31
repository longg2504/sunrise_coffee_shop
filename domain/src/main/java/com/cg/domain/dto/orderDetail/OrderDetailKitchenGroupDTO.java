package com.cg.domain.dto.orderDetail;

import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.unit.UnitDTO;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.Unit;
import com.cg.domain.enums.EOrderDetailStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderDetailKitchenGroupDTO {
    private Long orderDetailId;
    private Long productId;
    private String productTitle;
    private String note;
    private Long quantity;
    private String unitTitle;
    private String status;
    private String tableName;
    //com.cg.domain.dto.orderDetail.OrderDetailKitchenGroupDTO(java.lang.Long, java.lang.String, java.lang.String, java.lang.Long, java.lang.String)'


    public OrderDetailKitchenGroupDTO(Long orderDetailId,Long productId, String productTitle, String note, Long quantity, String unitTitle,EOrderDetailStatus status,String tableName) {
        this.orderDetailId = orderDetailId;
        this.productId = productId;
        this.productTitle = productTitle;
        this.note = note;
        this.quantity = quantity;
        this.unitTitle = unitTitle;
        this.status = String.valueOf(status);
        this.tableName = tableName;
    }
}
