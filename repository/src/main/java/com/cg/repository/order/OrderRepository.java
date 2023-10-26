package com.cg.repository.order;

import com.cg.domain.dto.order.IOrderDTO;
import com.cg.domain.dto.order.OrderDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.TableOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT ord FROM Order AS ord WHERE ord.tableOrder.id = :tableId AND ord.paid = false")
    Optional<Order> findByTableId(@Param("tableId") Long tableId);


    List<Order> findByTableOrderAndPaid(TableOrder tableOrder, Boolean paid);


    @Query("SELECT SUM(od.amount) FROM OrderDetail AS od WHERE od.order.id = :orderId")
    BigDecimal getOrderTotalAmount(@Param("orderId") Long orderId);


    @Query("SELECT NEW com.cg.domain.dto.order.OrderDTO (" +
            "od.id, " +
            "od.totalAmount, " +
            "od.tableOrder, " +
            "od.updatedAt " +
            ") " +
            "FROM Order AS od " +
            "WHERE od.deleted = false " +
            "AND od.paid = false "
    )
    List<OrderDTO> getOrderDTOByStatus();


    @Query(value = "SELECT * FROM v_get_order_by_status_cooking", nativeQuery = true)
    List<IOrderDTO> getOrderDTOByStatusCooking();


}
