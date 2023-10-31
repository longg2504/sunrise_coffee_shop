package com.cg.domain.dto.bill;

import com.cg.domain.entity.Bill;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.TableOrder;
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
public class BillResDTO {
    private BigDecimal orderPrice;

    private BigDecimal discountMoney;

    private Long discountPercent;

    private BigDecimal chargeMoney;

    private Long chargePercent;

    private BigDecimal totalAmount;

    private Long tableId;

    private Long orderId;

    private Boolean paid;

    public Bill toBill(TableOrder table, Order order) {
        return new Bill()
                .setDiscountPercent(discountPercent)
                .setChargePercent(chargePercent)
                .setTable(table)
                .setOrder(order)
                ;
    }

}
