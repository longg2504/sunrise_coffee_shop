package com.cg.domain.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class BillPrintItemDTO {
    private String productTitle;
    private Long quantity;
    private String unitTitle;
    private BigDecimal price;
    private BigDecimal amount;

    public BillPrintItemDTO(String productTitle, Long quantity, String unitTitle, BigDecimal price, BigDecimal amount) {
        this.productTitle = productTitle;
        this.quantity = quantity;
        this.unitTitle = unitTitle;
        this.price = price;
        this.amount = amount;
    }
}
