package com.cg.domain.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class PayDTO {
    private BigDecimal cashPay;
    private BigDecimal transferPay;

    public PayDTO(BigDecimal cashPay, BigDecimal transferPay) {
        this.cashPay = cashPay;
        this.transferPay = transferPay;
    }
}
