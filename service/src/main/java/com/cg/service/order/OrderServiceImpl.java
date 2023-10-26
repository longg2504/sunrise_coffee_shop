package com.cg.service.order;

import com.cg.domain.dto.order.*;
import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.*;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.repository.order.OrderRepository;
import com.cg.repository.orderDetail.OrderDetailRepository;
import com.cg.repository.product.ProductRepository;
import com.cg.repository.staff.StaffRepository;
import com.cg.repository.tableOrder.TableOrderRepository;
import com.cg.service.orderDetail.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private TableOrderRepository tableOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IOrderDetailService orderDetailService;


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<Order> findByTableId(Long tableId) {
        return orderRepository.findByTableId(tableId);
    }

    @Override
    public List<Order> findByTableOrderAndPaid(TableOrder tableOrder, Boolean paid) {
        return orderRepository.findByTableOrderAndPaid(tableOrder, paid);
    }

    @Override
    public OrderDetailCreResDTO creOrder(OrderCreReqDTO orderCreReqDTO, TableOrder tableOrder, User user) {
        Order order = new Order();
        Optional<Staff> optionalStaff = staffRepository.findByUserAndDeletedIsFalse(user);
        order.setStaff(optionalStaff.get());
        order.setTableOrder(tableOrder);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPaid(false);
        orderRepository.save(order);

        tableOrder.setStatus(ETableStatus.BUSY);
        tableOrderRepository.save(tableOrder);

        Product product = productRepository.findById(orderCreReqDTO.getProductId()).orElseThrow(() -> {
            throw new DataInputException("Sản phẩm này không tồn tại vui lòng xem lại");
        });

        OrderDetail orderDetail = new OrderDetail();
        Long quantity = orderCreReqDTO.getQuantity();
        Long quantityDelivery = 0L;
        BigDecimal price = product.getPrice();
        BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setQuantityDelivery(quantityDelivery);
        orderDetail.setPrice(price);
        orderDetail.setAmount(amount);
        orderDetail.setNote(orderCreReqDTO.getNote());
        orderDetail.setStatus(EOrderDetailStatus.NEW);
        orderDetail.setOrder(order);

        orderDetailRepository.save(orderDetail);

        order.setTotalAmount(amount);
        orderRepository.save(order);

        OrderDetailCreResDTO orderDetailCreResDTO = new OrderDetailCreResDTO();
        orderDetailCreResDTO.setOrderDetailId(orderDetail.getId());
        orderDetailCreResDTO.setTable(tableOrder.toTableOrderResDTO());
        orderDetailCreResDTO.setProductId(product.getId());
        orderDetailCreResDTO.setTitle(product.getTitle());
        orderDetailCreResDTO.setPrice(price);
        orderDetailCreResDTO.setQuantity(quantity);
        orderDetailCreResDTO.setQuantityDelivery(quantityDelivery);
        orderDetailCreResDTO.setAmount(amount);
        orderDetailCreResDTO.setNote(orderDetail.getNote());
        orderDetailCreResDTO.setTotalAmount(amount);
        orderDetailCreResDTO.setStatus(String.valueOf(orderDetail.getStatus()));
        orderDetailCreResDTO.setAvatar(product.getProductAvatar().toAvatarResDTO());
        return orderDetailCreResDTO;
    }

    @Override
    public OrderDetailUpResDTO upOrderDetail(OrderUpReqDTO orderUpReqDTO, Order order, Product product, User user) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder(order);
        OrderDetail orderDetail = new OrderDetail();
        if (orderDetails.size() == 0) {
            throw new DataInputException("Hoá đơn bàn này chưa có mặt hàng nào, vui lòng liên hệ ADMIN để kiểm tra lại dữ liệu");
        }

        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findByProductIdAndOrderIdAndNote(orderUpReqDTO.getProductId(), order.getId(), orderUpReqDTO.getNote(), orderUpReqDTO.getStatus());
        if (orderDetailOptional.isEmpty()) {
            Long quantity = orderUpReqDTO.getQuantity();
            Long quantityDelivery = 0L;
            BigDecimal price = product.getPrice();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setQuantity(quantity);
            orderDetail.setQuantityDelivery(quantityDelivery);
            orderDetail.setAmount(amount);
            orderDetail.setStatus(EOrderDetailStatus.NEW);
            orderDetail.setNote(orderUpReqDTO.getNote());
            orderDetailRepository.save(orderDetail);

            BigDecimal newTotalAmount = getOrderTotalAmount(order.getId());
            order.setTotalAmount(newTotalAmount);
            orderRepository.save(order);
        } else {
            orderDetail = orderDetailOptional.get();
            long newQuantity = orderDetail.getQuantity() + orderUpReqDTO.getQuantity();
            BigDecimal price = orderDetail.getPrice();
            BigDecimal newAmount = price.multiply(BigDecimal.valueOf(newQuantity));
            orderDetail.setQuantity(newQuantity);
            orderDetail.setQuantityDelivery(orderDetail.getQuantityDelivery());
            orderDetail.setAmount(newAmount);
            orderDetail.setStatus(EOrderDetailStatus.NEW);
            orderDetailRepository.save(orderDetail);

            BigDecimal newTotalAmount = getOrderTotalAmount(order.getId());
            order.setTotalAmount(newTotalAmount);
            orderRepository.save(order);
        }
        List<OrderDetailProductUpResDTO> newOrderDetails = orderDetailRepository.findAllOrderDetailProductUpResDTO(order.getId());

        OrderDetailUpResDTO orderDetailUpResDTO = new OrderDetailUpResDTO();
        orderDetailUpResDTO.setTable(order.getTableOrder().toTableOrderResDTO());
        orderDetailUpResDTO.setProducts(newOrderDetails);
        orderDetailUpResDTO.setTotalAmount(order.getTotalAmount());
        orderDetailUpResDTO.setStatus(EOrderDetailStatus.NEW);
        return orderDetailUpResDTO;
    }

    @Override
    public OrderChangeStatusResDTO upStatusOrderItemToWaiter(OrderChangeStatusReqDTO orderChangeStatusReqDTO, User user) {
        Long tableId = orderChangeStatusReqDTO.getTableId();
        Optional<Order> orderOptional = orderRepository.findByTableId(tableId);
        if (!orderOptional.isPresent()) {
            throw new DataInputException("Bàn này chưa có hoá đơn vui lòng xem lại!!!");
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder(orderOptional.get());
        if (orderDetails.isEmpty()) {
            throw new DataInputException("Hoá đơn bàn này chưa có mặt hàng nào, vui lòng liên hệ ADMIN để kiểm tra lại dữ liệu");
        }
        for (OrderDetail item : orderDetails) {
            if (item.getStatus() == EOrderDetailStatus.NEW) {
                item.setStatus(EOrderDetailStatus.COOKING);
                orderDetailRepository.save(item);
            }
        }

        List<OrderDetailChangeStatusResDTO> newOrderDetails = orderDetailRepository.findAllOrderDetailByStatus(orderOptional.get().getId(), EOrderDetailStatus.COOKING);
        OrderChangeStatusResDTO orderChangeStatusResDTO = new OrderChangeStatusResDTO();
        orderChangeStatusResDTO.setOrderDetails(newOrderDetails);
        orderChangeStatusResDTO.setTotalAmount(orderOptional.get().getTotalAmount());
        orderChangeStatusResDTO.setTableId(tableId);
        return orderChangeStatusResDTO;

    }

    @Override
    public BigDecimal getOrderTotalAmount(Long orderId) {
        return orderRepository.getOrderTotalAmount(orderId);
    }

    @Override
    public List<OrderKitchenTableDTO> getAllOrderKitchenCookingByTable(EOrderDetailStatus status) {
        List<OrderKitchenTableDTO> orderList = new ArrayList<>();

        List<IOrderDTO> orderDTOList = getOrderDTOByStatusCooking();


        for (IOrderDTO item : orderDTOList) {
            List<IOrderDetailKitchenTableDTO> orderItemList = orderDetailService.getOrderItemByStatusCookingAndTable(item.getTableId());
            if (orderItemList.size() != 0) {
                int countProduct = this.countProductInOrderItem(orderItemList);

                OrderKitchenTableDTO orderKitchenDTO = new OrderKitchenTableDTO()
                        .setOrderId(item.getId())
                        .setTableId(item.getTableId())
                        .setTableName(item.getTableName())
                        .setCountProduct(countProduct)
                        .setUpdatedAt(item.getUpdatedAt())
                        .setOrderDetails(orderItemList);
                orderList.add(orderKitchenDTO);
            }
        }
        return orderList;

    }

    @Override
    public List<OrderDTO> getOrderDTOByStatus() {
        return orderRepository.getOrderDTOByStatus();
    }

    @Override
    public List<IOrderDTO> getOrderDTOByStatusCooking() {
        return orderRepository.getOrderDTOByStatusCooking();
    }

    @Override
    public int countProductInOrder(List<OrderDetailKitchenTableDTO> orderItemList) {
        int count = 0;
        for (OrderDetailKitchenTableDTO item : orderItemList) {
            count += item.getQuantity();
        }
        return count;

    }

    @Override
    public int countProductInOrderItem(List<IOrderDetailKitchenTableDTO> orderItemList) {
        int count = 0;
        for (IOrderDetailKitchenTableDTO item : orderItemList) {
            count += item.getQuantity();
        }
        return count;

    }

}
