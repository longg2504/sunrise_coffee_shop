package com.cg.service.order;

import com.cg.domain.dto.order.*;
import com.cg.domain.dto.orderDetail.IOrderDetailKitchenTableDTO;
import com.cg.domain.dto.orderDetail.OrderDetailCreResDTO;
import com.cg.domain.dto.orderDetail.OrderDetailKitchenTableDTO;
import com.cg.domain.dto.orderDetail.OrderDetailUpResDTO;
import com.cg.domain.entity.*;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IOrderService extends IGeneralService<Order,Long> {
    Optional<Order> findByTableId(Long tableId);

    List<Order> findByTableOrderAndPaid(TableOrder tableOrder, Boolean paid);

    OrderDetailCreResDTO creOrder(OrderCreReqDTO orderCreReqDTO, TableOrder tableOrder, User user);

    OrderDetailUpResDTO upOrderDetail(OrderUpReqDTO orderUpReqDTO, Order order, Product product, User user);

    OrderChangeStatusResDTO upStatusOrderItemToWaiter(OrderChangeStatusReqDTO orderChangeStatusReqDTO ,User user);


    BigDecimal getOrderTotalAmount(Long orderId);

    List<OrderKitchenTableDTO> getAllOrderKitchenCookingByTable(EOrderDetailStatus status);

    List<OrderDTO> getOrderDTOByStatus();
    List<IOrderDTO> getOrderDTOByStatusCooking();

    int countProductInOrder(List<OrderDetailKitchenTableDTO> orderItemList);

    int countProductInOrderItem(List<IOrderDetailKitchenTableDTO> orderItemList);



}
