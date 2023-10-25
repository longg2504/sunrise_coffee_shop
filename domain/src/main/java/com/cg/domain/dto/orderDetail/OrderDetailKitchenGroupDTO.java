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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderDetailKitchenGroupDTO {
    private ProductDTO product;
    private String note;
    private Long quantity;
    private String status;

    public OrderDetailKitchenGroupDTO(Product product, String note, Long quantity, EOrderDetailStatus status) {
        this.product = product.toProductDTO();
        this.note = note;
        this.quantity = quantity;
        this.status = String.valueOf(status);
    }
}
