package com.cg.service.order;

import com.cg.domain.dto.order.OrderChangeStatusReqDTO;
import com.cg.domain.dto.order.OrderChangeStatusResDTO;
import com.cg.domain.dto.order.OrderCreReqDTO;
import com.cg.domain.dto.order.OrderUpReqDTO;
import com.cg.domain.dto.orderDetail.OrderDetailCreResDTO;
import com.cg.domain.dto.orderDetail.OrderDetailUpResDTO;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.User;
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


}
