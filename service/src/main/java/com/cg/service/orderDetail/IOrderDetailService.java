package com.cg.service.orderDetail;

import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailService extends IGeneralService<OrderDetail,Long> {
    List<OrderDetailByTableResDTO> getOrderDetailByTableResDTO(Long orderId);

    Optional<OrderDetail> findByOrderDetailByIdProductAndIdOrder(Long idProduct, Long idOrder, String note, String status);

    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusWaiter(Long orderId, Long productId, String note);
    Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusDone(Long orderId, Long productId, String note);

    OrderDetail findByOrderId(Long orderId);
    List<OrderDetailKitchenGroupDTO> getOrderItemByStatusGroupByProduct(EOrderDetailStatus orderDetailStatus);
    List<IOrderDetailKitchenGroupDTO> getOrderDetailByStatusCookingGroupByProduct();
    List<OrderDetailKitchenWaiterDTO> getOrderDetailByStatusWaiterGroupByTableAndProduct(EOrderDetailStatus orderDetailStatus);
    List<IOrderDetailKitchenWaiterDTO> getOrderDetailByStatusWaiterGroupByTableAndProduct();
    List<OrderDetailKitchenTableDTO> getOrderDetailByStatusAndTable(EOrderDetailStatus orderDetailStatus, Long tableId);
    List<IOrderDetailKitchenTableDTO> getOrderItemByStatusCookingAndTable(Long tableId);


    OrderDetailKitchenWaiterDTO changeStatusFromCookingToWaiterOfProduct(OrderDetail orderItemCooking);
}
