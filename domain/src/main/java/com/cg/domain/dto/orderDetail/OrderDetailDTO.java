package com.cg.domain.dto.orderDetail;

import com.cg.domain.dto.product.ProductDTO;

import java.math.BigDecimal;

public class OrderDetailDTO {
    private Long orderDetailId;
    private ProductDTO product;
    private String quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private String note;

    private String status;
}
