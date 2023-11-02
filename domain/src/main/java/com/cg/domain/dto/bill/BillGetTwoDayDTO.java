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
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class BillGetTwoDayDTO {
    private Long id;

    private Date createdAt;

    private BigDecimal orderPrice;

    private BigDecimal discountMoney;
    private Long discountPercent;

    private BigDecimal chargeMoney;
    private Long chargePercent;

    private BigDecimal totalAmount;

    private Long tableId;
    private String tableName;

    private Long orderId;

    private Long staffId;
    private String staffName;

    private Boolean paid;

    public Bill toBill(TableOrder table, Order order) {
        return new Bill()
                .setDiscountPercent(discountPercent)
                .setChargePercent(chargePercent)
                .setTable(table)
                .setOrder(order)
                ;
    }

    public BillGetTwoDayDTO(Long id, Date createdAt, BigDecimal orderPrice, BigDecimal discountMoney, Long discountPercent, BigDecimal chargeMoney, Long chargePercent, BigDecimal totalAmount, Long tableId, String tableName, Long orderId, Long staffId, String staffName, Boolean paid) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderPrice = orderPrice;
        this.discountMoney = discountMoney;
        this.discountPercent = discountPercent;
        this.chargeMoney = chargeMoney;
        this.chargePercent = chargePercent;
        this.totalAmount = totalAmount;
        this.tableId = tableId;
        this.tableName = tableName;
        this.orderId = orderId;
        this.staffId = staffId;
        this.staffName = staffName;
        this.paid = paid;
    }
}
