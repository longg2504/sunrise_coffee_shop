package com.cg.domain.dto.orderDetail;

import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.entity.Product;
import com.cg.domain.enums.EOrderDetailStatus;
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
public class OrderDetailDTO {
    private Long orderDetailId;
    private ProductDTO product;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private String note;
    private String status;

    public OrderDetailDTO(Long orderDetailId, Product product, Long quantity, BigDecimal price, BigDecimal amount, String note, EOrderDetailStatus status) {
        this.orderDetailId = orderDetailId;
        this.product = product.toProductDTO();
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;
        this.note = note;
        this.status = String.valueOf(status);
    }

    public OrderDetail toOrderDetail(){
        return new OrderDetail()
                .setId(orderDetailId)
                .setPrice(price)
                .setQuantity(quantity)
                .setStatus(EOrderDetailStatus.valueOf(status))
                .setAmount(amount)
                .setNote(note)
                .setProduct(product.toProduct())
                ;
    }

}
