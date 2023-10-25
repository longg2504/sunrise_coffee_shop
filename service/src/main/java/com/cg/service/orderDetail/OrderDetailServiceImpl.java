package com.cg.service.orderDetail;

import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.repository.order.OrderRepository;
import com.cg.repository.orderDetail.OrderDetailRepository;
import com.cg.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderDetailServiceImpl implements IOrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IOrderService orderService;
    @Override
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void delete(OrderDetail orderDetail) {
        Order order = orderDetail.getOrder();
        orderDetailRepository.delete(orderDetail);

        order.setOrderDetails(order.getOrderDetails()
                .stream().filter(e -> !Objects.equals(e.getId(), orderDetail.getId()))
                .collect(Collectors.toList()));

        if (order.getOrderDetails().size() == 0) {
            orderRepository.delete(order);
        }
        else {
            BigDecimal totalAmount = orderService.getOrderTotalAmount(order.getId());
            order.setTotalAmount(totalAmount);
            orderService.save(order);
        }

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<OrderDetailByTableResDTO> getOrderDetailByTableResDTO(Long orderId) {
        return orderDetailRepository.getOrderDetailByTableResDTO(orderId);
    }

    @Override
    public Optional<OrderDetail> findByOrderDetailByIdProductAndIdOrder(Long idProduct, Long idOrder, String note, String status) {
        return findByOrderDetailByIdProductAndIdOrder(idProduct,idOrder,note,status);
    }

    @Override
    public OrderDetail findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderDetailKitchenGroupDTO> getOrderItemByStatusGroupByProduct(EOrderDetailStatus orderDetailStatus) {
        return orderDetailRepository.getOrderItemByStatusGroupByProduct(orderDetailStatus);
    }

    @Override
    public List<IOrderDetailKitchenGroupDTO> getOrderDetailByStatusCookingGroupByProduct() {
        return orderDetailRepository.getOrderDetailByStatusCookingGroupByProduct();
    }

    @Override
    public List<OrderDetailKitchenWaiterDTO> getOrderDetailByStatusWaiterGroupByTableAndProduct(EOrderDetailStatus orderDetailStatus) {
        return orderDetailRepository.getOrderDetailByStatusWaiterGroupByTableAndProduct(orderDetailStatus);
    }

    @Override
    public List<IOrderDetailKitchenWaiterDTO> getOrderDetailByStatusWaiterGroupByTableAndProduct() {
        return orderDetailRepository.getOrderDetailByStatusWaiterGroupByTableAndProduct();
    }

    @Override
    public List<OrderDetailKitchenTableDTO> getOrderDetailByStatusAndTable(EOrderDetailStatus orderDetailStatus, Long tableId) {
        return orderDetailRepository.getOrderDetailByStatusAndTable(orderDetailStatus,tableId);
    }

    @Override
    public List<IOrderDetailKitchenTableDTO> getOrderItemByStatusCookingAndTable(Long tableId) {
        return orderDetailRepository.getOrderItemByStatusCookingAndTable(tableId);
    }
}
