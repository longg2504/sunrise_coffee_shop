package com.cg.repository.bill;

import com.cg.domain.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT NEW com.cg.domain.entity.Bill (" +
            "bi.id," +
            "bi.orderPrice," +
            "bi.discountMoney," +
            "bi.discountPercent," +
            "bi.chargeMoney," +
            "bi.chargePercent," +
            "bi.totalAmount," +
            "bi.table," +
            "bi.staff," +
            "bi.order," +
            "bi.paid," +
            "bi.cashPay," +
            "bi.transferPay " +
            ") " +
            "FROM Bill AS bi " +
            "WHERE bi.deleted = false " +
            "AND bi.order.id = :orderId "

    )
    Optional<Bill> findBillByOrderId(@Param("orderId") Long orderId);



}
