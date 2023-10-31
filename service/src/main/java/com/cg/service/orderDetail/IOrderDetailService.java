package com.cg.service.orderDetail;

import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailService extends IGeneralService<OrderDetail,Long> {
    List<OrderDetailByTableResDTO> getOrderDetailByTableResDTO(Long orderId);

    Optional<OrderDetail> findByOrderDetailByIdProductAndIdOrder(Long idProduct, Long idOrder, String note, String status);

    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusCooking(Long orderId, Long productId, String note);

    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusWaiting(Long orderId, Long productId, String note);
    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusDone(Long orderId, Long productId, String note);
    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusStock(Long orderId, Long productId, String note);
    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusDelivery(Long orderId, Long productId, String note);

    List<OrderDetail> findAllByOrderAndStatus(Order order, EOrderDetailStatus status);

    OrderDetail findByOrderId(Long orderId);
    List<OrderDetailResDTO> getOrderDetailResDTOByOrderId(Long orderId);
    List<OrderDetailKitchenGroupDTO> getOrderItemByStatusGroupByProduct(EOrderDetailStatus orderDetailStatus);
    List<IOrderDetailKitchenGroupDTO> getOrderDetailByStatusCookingGroupByProduct();
    List<OrderDetailKitchenWaiterDTO> getOrderDetailWaiterGroupByTableAndProduct(EOrderDetailStatus orderDetailStatus);
    List<IOrderDetailKitchenWaiterDTO> getOrderDetailByStatusWaiterGroupByTableAndProduct();
    List<OrderDetailKitchenTableDTO> getOrderDetailByStatusAndTable(EOrderDetailStatus orderDetailStatus, Long tableId);
    List<IOrderDetailKitchenTableDTO> getOrderItemByStatusCookingAndTable(Long tableId);


    OrderDetailKitchenWaiterDTO changeStatusFromCookingToWaiterOfProduct(OrderDetail orderItemCooking);
    
    void changeStatusFromCookingToWaitingAllToProductOfOrder(OrderDetail orderDetail);

    void changeStatusFromCookingToWaitingAllToProductOfOrderOfTable(Order order);

    OrderDetailKitchenWaiterDTO changeStatusFromCookingToStockOutOfProduct(OrderDetail orderDetailCooking);

    void changeStatusFromCookingToStockOutToProduct(OrderDetail orderDetailCooking);

    OrderDetailKitchenWaiterDTO changeStatusFromCookingToWaiterToOneProductOfGroup(Long productId, String note);

    List<OrderDetailKitchenWaiterDTO> changeStatusFromCookingToWaiterToAllProductsOfGroup(Long productId, String note);

    void changeStatusFromWaiterToDeliveryOfProduct(OrderDetail orderDetailWaiter);

    void changeStatusFromWaiterToDeliveryToProductOfOrder(OrderDetail orderDetailWaiter);

    void changeStatusFromWaiterToDeliveryAllProductOfTable(Order order);

    void changeStatusFromWaiterToStockOutOfProduct(OrderDetail orderDetailWaiter);

    void changeStatusFromWaiterToStockOutToProductOfOrder(OrderDetail orderDetailWaiter);

    void changeStatusFromDeliveryToDoneOfProduct(OrderDetail orderDetailDelivery);

    void changeStatusFromDeliveryToDoneToProductOfOrder(OrderDetail orderDetailDelivery);

    void changeStatusFromDeliveryToDoneToTableAll(Order order);

    void deleteOrderDetailStockOut(OrderDetail orderDetail);






}
