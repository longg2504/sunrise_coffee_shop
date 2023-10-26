package com.cg.service.orderDetail;

import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.entity.Product;
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
    public Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusWaiter(Long orderId, Long productId, String note) {
        return orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(orderId, productId, note, EOrderDetailStatus.WAITING);
    }

    @Override
    public Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusDone(Long orderId, Long productId, String note) {
        return orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(orderId, productId, note, EOrderDetailStatus.DONE);

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

    @Override
    public OrderDetailKitchenWaiterDTO changeStatusFromCookingToWaiterOfProduct(OrderDetail orderItemCooking) {
        Order order = orderItemCooking.getOrder();
        Product product = orderItemCooking.getProduct();
        String note = orderItemCooking.getNote();

        Optional<OrderDetail> orderDetailOptional = this.findByOrderIdAndProductIdAndNoteAndStatusWaiter(order.getId(), product.getId(), note);

        OrderDetailKitchenWaiterDTO orderDetailKitchenWaiterDTO;
        if(!orderDetailOptional.isPresent()){
            if(orderItemCooking.getQuantity() == 1){
                orderItemCooking.setStatus(EOrderDetailStatus.WAITING);
                orderDetailRepository.save(orderItemCooking);

                orderDetailKitchenWaiterDTO = orderItemCooking.toOrderItemKitchenWaiterDTO();
            }
            else {
                Long newQuantityCooking = orderItemCooking.getQuantity() - 1 ;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderDetailRepository.save(orderItemCooking);

                OrderDetail newOrdetDetail = new OrderDetail();


                orderDetailKitchenWaiterDTO = orderItemCooking.toOrderItemKitchenWaiterDTO();

            }
        }
        else {
            OrderDetail newOrderDetail = orderDetailOptional.get();

            if(orderItemCooking.getQuantity() == 1){
                orderDetailRepository.deleteById(orderItemCooking.getId());
            }
            else{
                Long newQuantityCooking = orderItemCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityCooking));
                orderItemCooking.setQuantity(newQuantityCooking);
                orderItemCooking.setAmount(newAmountCooking);
                orderDetailRepository.save(orderItemCooking);

            }

            Long newQuantityWaiter = orderItemCooking.getQuantity() + 1;
            BigDecimal newAmountWaiter = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
            newOrderDetail.setQuantity(newQuantityWaiter);
            newOrderDetail.setAmount(newAmountWaiter);
            orderDetailRepository.save(newOrderDetail);

            orderDetailKitchenWaiterDTO = newOrderDetail.toOrderItemKitchenWaiterDTO();

        }
        return orderDetailKitchenWaiterDTO;
    }
}
