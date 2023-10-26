package com.cg.repository.orderDetail;

import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.domain.enums.ETableStatus;
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
            "od.quantityDelivery," +
            "od.amount, " +
            "od.product.unit ," +
            "od.note," +
            "od.status," +
            "od.product.productAvatar" +
            ") " +
            "FROM OrderDetail AS od " +
            "WHERE od.order.id = :orderId " +
            "AND od.deleted = false "
    )
    List<OrderDetailByTableResDTO> getOrderDetailByTableResDTO(@Param("orderId") Long orderId);

    List<OrderDetail> findAllByOrder(Order order);

    @Query("SELECT NEW com.cg.domain.dto.orderDetail.OrderDetailProductUpResDTO (" +
            "od.id, " +
            "od.product.id, " +
            "od.product.title, " +
            "od.price, " +
            "od.quantity," +
            "od.quantityDelivery, " +
            "od.amount, " +
            "od.note," +
            "od.status, " +
            "od.product.productAvatar" +
            ") " +
            "FROM OrderDetail AS od " +
            "WHERE od.order.id = :orderId " +
            "AND od.deleted = false"
    )
    List<OrderDetailProductUpResDTO> findAllOrderDetailProductUpResDTO(@Param("orderId") Long orderId);


    @Query("SELECT NEW com.cg.domain.dto.orderDetail.OrderDetailChangeStatusResDTO (" +
            "od.id, " +
            "od.product.id, " +
            "od.product.title, " +
            "od.price," +
            "od.count, " +
            "od.quantity ," +
            "od.quantityDelivery, " +
            "od.amount," +
            "od.note," +
            "od.status," +
            "od.product.productAvatar" +
            ") " +
            "FROM OrderDetail AS od " +
            "WHERE od.order.id = :orderId " +
            "AND od.deleted = false " +
            "AND od.status = :orderDetailStatus"
    )

    List<OrderDetailChangeStatusResDTO> findAllOrderDetailByStatus(@Param("orderId") Long orderId, @Param("orderDetailStatus")EOrderDetailStatus orderDetailStatus);
    @Query("SELECT od " +
            "FROM OrderDetail AS od " +
            "WHERE od.order.id = :orderId " +
            "AND od.product.id = :productId " +
            "AND od.note = :note " +
            "AND od.status = :orderDetailStatus ")
    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(@Param("orderId") Long orderId, @Param("productId") Long productId, @Param("note") String note, @Param("orderDetailStatus") EOrderDetailStatus orderDetailStatus);


    @Query("SELECT odt FROM OrderDetail AS odt WHERE odt.order.id = :orderId")
    OrderDetail findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT odt " +
            "FROM OrderDetail AS odt " +
            "WHERE odt.product.id = :idProduct " +
            "AND odt.order.id = :idOrder " +
            "AND odt.note LIKE :note " +
            "AND odt.status = :status"
    )
    Optional<OrderDetail> findByProductIdAndOrderIdAndNote(@Param("idProduct") Long idProduct, @Param("idOrder") Long idOrder, @Param("note") String note, @Param("status") EOrderDetailStatus status );

    @Query("SELECT SUM(odt.amount) FROM OrderDetail AS odt WHERE odt.order.id = :orderId")
    BigDecimal findByOrderByIdSumAmount(@Param("orderId") Long orderId);



    @Query("SELECT NEW com.cg.domain.dto.orderDetail.OrderDetailKitchenGroupDTO (" +
            "od.product.id, " +
            "od.product.title, " +
            "od.note, " +
            "SUM(od.count), " +
            "SUM(od.quantity), " +
            "od.product.unit.title" +
            ") " +
            "FROM OrderDetail AS od " +
            "JOIN Product AS pd " +
            "ON od.product.id = pd.id " +
            "WHERE od.status = :orderDetailStatus " +
            "GROUP BY od.product.id, od.note "
    )
    List<OrderDetailKitchenGroupDTO> getOrderItemByStatusGroupByProduct(@Param("orderDetailStatus") EOrderDetailStatus orderDetailStatus);

    @Query(value = "SELECT * FROM v_get_order_detail_by_status_cooking_group_by_product", nativeQuery = true)
    List<IOrderDetailKitchenGroupDTO> getOrderDetailByStatusCookingGroupByProduct();

    @Query("SELECT NEW com.cg.domain.dto.orderDetail.OrderDetailKitchenWaiterDTO (" +
            "od.id," +
            "od.order.tableOrder.id," +
            "od.order.tableOrder.title," +
            "od.product.id," +
            "od.product.title," +
            "od.note," +
            "SUM(od.count), " +
            "SUM(od.quantity)," +
            "od.product.unit.title," +
            "od.status," +
            "od.updatedAt " +
            ") " +
            "FROM OrderDetail AS od " +
            "JOIN Product AS pd " +
            "ON od.product.id = pd.id " +
            "WHERE od.status = :orderDetailStatus " +
            "GROUP BY od.order.tableOrder.id, od.product.id, od.note "
    )
    List<OrderDetailKitchenWaiterDTO> getOrderDetailByStatusWaiterGroupByTableAndProduct(@Param("orderDetailStatus") EOrderDetailStatus orderDetailStatus);

    @Query(value = "SELECT * FROM v_get_order_detail_waiter_group_by_table_and_product", nativeQuery = true)
    List<IOrderDetailKitchenWaiterDTO> getOrderDetailByStatusWaiterGroupByTableAndProduct();

    @Query("SELECT NEW com.cg.domain.dto.orderDetail.OrderDetailKitchenTableDTO(" +
            "od.id, " +
            "od.order.tableOrder.title, " +
            "od.product.id, " +
            "od.product.title, " +
            "od.note, " +
            "od.count," +
            "od.quantity," +
            "od.product.unit.title," +
            "od.status," +
            "od.updatedAt" +
            ") " +
            "FROM OrderDetail AS od " +
            "JOIN Product AS pd " +
            "on od.product.id = pd.id " +
            "JOIN TableOrder AS t " +
            "ON od.order.tableOrder.id = t.id " +
            "WHERE od.status = :orderDetailStatus " +
            "AND od.order.tableOrder.id = :tableId " +
            "ORDER by od.updatedAt ASC "
    )
    List<OrderDetailKitchenTableDTO> getOrderDetailByStatusAndTable(@Param("orderDetailStatus") EOrderDetailStatus orderDetailStatus , @Param("tableId") Long tableId);

    @Query(value = "CALL sp_get_order_detail_by_status_cooking_and_table(:tableId)", nativeQuery = true)
    List<IOrderDetailKitchenTableDTO> getOrderItemByStatusCookingAndTable(@Param("tableId") Long tableId);


    @Query("Select o FROM OrderDetail o WHERE o.order.tableOrder.id = :id AND o.status = :status" )
    List<OrderDetail> getOrder(Long id, EOrderDetailStatus status);
}
