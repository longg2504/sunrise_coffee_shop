package com.cg.domain.entity;

import com.cg.domain.dto.bill.*;
import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.dto.orderDetail.OrderItemBillResDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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


    public BillResDTO toBillResDTO() {
        return new BillResDTO()
                .setOrderPrice(orderPrice)
                .setDiscountMoney(discountMoney)
                .setDiscountPercent(discountPercent)
                .setChargeMoney(chargeMoney)
                .setChargePercent(chargePercent)
                .setTotalAmount(totalAmount)
                .setTableId(table.getId())
                .setOrderId(order.getId())
                .setPaid(paid)
                ;
    }




    public BillGetAllResDTO toBillGetAllResDTO( ) {
        return new BillGetAllResDTO()
                .setId(id)
                .setOrderPrice(orderPrice)
                .setDiscountMoney(discountMoney)
                .setDiscountPercent(discountPercent)
                .setChargeMoney(chargeMoney)
                .setChargePercent(chargePercent)
                .setTotalAmount(totalAmount)
                .setTableId(table.getId())
                .setTableName(table.getTitle())
                .setOrderId(order.getId())
                .setStaffId(staff.getId())
                .setStaffName(staff.getFullName())
                .setCreatedAt(getCreatedAt())
                .setPaid(paid)
                .setCashPay(cashPay)
                .setTransferPay(transferPay)
                ;
    }

    public BillPayResDTO toBillPayResDTO(List<OrderItemBillResDTO> orderItemDTOList) {
        return new BillPayResDTO()
                .setTableId(table.getId())
                .setTotalAmount(totalAmount)
                .setOrderId(order.getId())
                .setItems(orderItemDTOList)
                ;
    }

    public BillPrintTempDTO toBillPrintTempDTO(List<BillPrintItemDTO> itemDTOS, Date createdAt) {
        return new BillPrintTempDTO()
                .setOrderId(order.getId())
                .setTableId(table.getId())
                .setTableName(table.getTitle())
                .setCreatedAt(order.getCreatedAt())
                .setUpdatedAt(getUpdatedAt())
                .setStaffName(staff.getFullName())
                .setDiscountMoney(discountMoney)
                .setDiscountPercent(discountPercent)
                .setChargeMoney(chargeMoney)
                .setChargePercent(chargePercent)
                .setTotalAmount(totalAmount)
                .setItems(itemDTOS)
                ;
    }

    public Bill initBillPrePay(Order currentOrder, Staff currentStaff) {
        return new Bill()
                .setId(id)
                .setOrder(currentOrder)
                .setStaff(currentStaff)
                .setTable(currentOrder.getTableOrder())
                .setOrderPrice(currentOrder.getTotalAmount())
                .setChargePercent(0L)
                .setChargeMoney(BigDecimal.ZERO)
                .setDiscountPercent(0L)
                .setDiscountMoney(BigDecimal.ZERO)
                .setTotalAmount(currentOrder.getTotalAmount())
                .setPaid(false)
                ;

    }




}
