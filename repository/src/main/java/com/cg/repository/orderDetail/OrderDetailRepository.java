package com.cg.repository.orderDetail;

import com.cg.domain.dto.orderDetail.OrderDetailByTableResDTO;
import com.cg.domain.dto.orderDetail.OrderDetailProductUpResDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    @Query("SELECT NEW com.cg.domain.dto.orderDetail.OrderDetailByTableResDTO(" +
            "od.id, " +
            "od.product.id, " +
            "od.product.title, " +
            "od.price, " +
            "od.quantity, " +
            "od.amount, " +
            "od.product.unit ," +
            "od.note," +
            "od.status," +
            "od.product.productAvatar" +
            ") " +
            "FROM OrderDetail AS od " +
            "WHERE od.order.id = :orderId"
    )
    List<OrderDetailByTableResDTO> getOrderDetailByTableResDTO(@Param("orderId") Long orderId);

    List<OrderDetail> findAllByOrder(Order order);

    @Query("SELECT NEW com.cg.domain.dto.orderDetail.OrderDetailProductUpResDTO (" +
            "od.id, " +
            "od.product.id, " +
            "od.product.title, " +
            "od.price, " +
            "od.quantity, " +
            "od.amount, " +
            "od.note," +
            "od.status, " +
            "od.product.productAvatar" +
            ") " +
            "FROM OrderDetail AS od " +
            "WHERE od.order.id = :orderId"
    )
    List<OrderDetailProductUpResDTO> findAllOrderDetailProductUpResDTO(@Param("orderId") Long orderId);


    @Query("SELECT odt FROM OrderDetail AS odt WHERE odt.order.id = :orderId")
    OrderDetail findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT odt " +
            "FROM OrderDetail AS odt " +
            "WHERE odt.product.id = :idProduct " +
            "AND odt.order.id = :idOrder " +
            "AND odt.note LIKE :note "
    )
    Optional<OrderDetail> findByProductIdAndOrderIdAndNote(@Param("idProduct") Long idProduct, @Param("idOrder") Long idOrder, @Param("note") String note);

    @Query("SELECT SUM(odt.amount) FROM OrderDetail AS odt WHERE odt.order.id = :orderId")
    BigDecimal findByOrderByIdSumAmount(@Param("orderId") Long orderId);
}
