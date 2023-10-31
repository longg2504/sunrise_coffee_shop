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
public class BillCreateDTO {
    private Long discountPercent;

    private Long chargePercent;

    private Long orderId;



    public BillCreateDTO(BigDecimal discountMoney, Long discountPercent, BigDecimal chargeMoney, Long chargePercent, Order order) {
        this.discountPercent = discountPercent;
        this.chargePercent = chargePercent;
        this.orderId = order.getId();
    }

    public Bill toBill(TableOrder table, Order order) {
        return new Bill()
                .setDiscountPercent(discountPercent)
                .setChargePercent(chargePercent)
                .setTable(table)
                .setOrder(order)
                ;
    }

}
