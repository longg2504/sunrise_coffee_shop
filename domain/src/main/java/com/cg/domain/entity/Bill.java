package com.cg.domain.entity;

import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="bills")
@Accessors(chain = true)
public class Bill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_price", precision = 12, scale = 0, nullable = false)
    private BigDecimal orderPrice;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal discountMoney;

    private Long discountPercent;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal chargeMoney;

    private Long chargePercent;

    @Column(name = "total_amount", precision = 12 , scale = 0 , nullable = false)
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "table_order_id", referencedColumnName = "id", nullable = false)
    private TableOrder table;

    @ManyToOne
    @JoinColumn(name ="staff_id" , referencedColumnName = "id" , nullable = false)
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "order_id" , referencedColumnName = "id" , nullable = false)
    private Order order;

    private Boolean paid;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal cashPay;

    @Column(precision = 12, scale = 0, nullable = false)
    private BigDecimal transferPay;


    public Bill(Long id, BigDecimal orderPrice, BigDecimal discountMoney, Long discountPercent, BigDecimal chargeMoney, Long chargePercent, BigDecimal totalAmount, TableOrder table, Staff staff, Order order, Boolean paid, BigDecimal cashPay, BigDecimal transferPay) {
        this.id = id;
        this.orderPrice = orderPrice;
        this.discountMoney = discountMoney;
        this.discountPercent = discountPercent;
        this.chargeMoney = chargeMoney;
        this.chargePercent = chargePercent;
        this.totalAmount = totalAmount;
        this.table = table;
        this.staff = staff;
        this.order = order;
        this.paid = paid;
        this.cashPay = cashPay;
        this.transferPay = transferPay;
    }
}
