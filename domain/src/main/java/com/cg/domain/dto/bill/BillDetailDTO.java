package com.cg.domain.dto.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class BillDetailDTO {
    private Long billId;
    private BigDecimal totalAmount;
    private String note;
    private BigDecimal price;
    private Long quantity;
    private String productTitle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    public BillDetailDTO(Long billId, BigDecimal totalAmount, String note, BigDecimal price, Long quantity, String productTitle, Date createdAt) {
        this.billId = billId;
        this.totalAmount = totalAmount;
        this.note = note;
        this.price = price;
        this.quantity = quantity;
        this.productTitle = productTitle;
        this.createdAt = createdAt;
    }
}
