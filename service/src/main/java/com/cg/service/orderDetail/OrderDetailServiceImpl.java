package com.cg.service.orderDetail;

import com.cg.domain.dto.bill.BillPrintItemDTO;
import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.entity.Product;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.exception.DataInputException;
import com.cg.repository.order.OrderRepository;
import com.cg.repository.orderDetail.OrderDetailRepository;
import com.cg.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusCooking(Long orderId, Long productId, String note) {
        return orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(orderId, productId, note, EOrderDetailStatus.COOKING);
    }

    @Override
    public Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusWaiting(Long orderId, Long productId, String note) {
        return orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(orderId, productId, note, EOrderDetailStatus.WAITING);
    }

    @Override
    public Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusDone(Long orderId, Long productId, String note) {
        return orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(orderId, productId, note, EOrderDetailStatus.DONE);

    }

    @Override
    public Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusStock(Long orderId, Long productId, String note) {
        return orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(orderId, productId, note, EOrderDetailStatus.STOCK_OUT);
    }

    @Override
    public Optional<OrderDetail> findByOrderIdAndProductIdAndNoteAndStatusDelivery(Long orderId, Long productId, String note) {
        return orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(orderId, productId, note, EOrderDetailStatus.DELIVERY);
    }

    @Override
    public List<OrderDetail> findAllByOrderAndStatus(Order order, EOrderDetailStatus status) {
        return orderDetailRepository.findAllByOrderAndStatus(order,status);
    }



    @Override
    public OrderDetail findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderDetailResDTO> getOrderDetailResDTOByOrderId(Long orderId) {
        return orderDetailRepository.getOrderDetailResDTOByOrderId(orderId);
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
    public List<OrderDetailKitchenWaiterDTO> getOrderDetailWaiterGroupByTableAndProduct(EOrderDetailStatus orderDetailStatus) {
        return orderDetailRepository.getOrderDetailWaiterGroupByTableAndProduct(orderDetailStatus);
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

        Optional<OrderDetail> orderDetailOptional = this.findByOrderIdAndProductIdAndNoteAndStatusWaiting(order.getId(), product.getId(), note);

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

                OrderDetail newOrdetDetail = new OrderDetail()
                        .setId(null)
                        .setOrder(orderItemCooking.getOrder())
                        .setProduct(orderItemCooking.getProduct())
                        .setQuantity(1L)
                        .setStatus(EOrderDetailStatus.WAITING)
                        .setNote(orderItemCooking.getNote())
                        .setPrice(orderItemCooking.getPrice())
                        .setAmount(newAmountCooking)
                        ;
                orderDetailRepository.save(newOrdetDetail);
                orderDetailKitchenWaiterDTO = newOrdetDetail.toOrderItemKitchenWaiterDTO();

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

                Long newQuantityWaiter = newOrderDetail.getQuantity() + 1;
                BigDecimal newAmountWaiter = orderItemCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                newOrderDetail.setQuantity(newQuantityWaiter);
                newOrderDetail.setAmount(newAmountWaiter);
                orderDetailRepository.save(newOrderDetail);

                orderDetailKitchenWaiterDTO = newOrderDetail.toOrderItemKitchenWaiterDTO();
            }
        return orderDetailKitchenWaiterDTO;
    }

    @Override
    public void changeStatusFromCookingToWaitingAllToProductOfOrder(OrderDetail orderDetail) {
        Order order = orderDetail.getOrder();
        Product product = orderDetail.getProduct();
        String note = orderDetail.getNote();

        Optional<OrderDetail> orderDetailWaiter = this.findByOrderIdAndProductIdAndNoteAndStatusWaiting(order.getId(), product.getId(),note);

        if(orderDetailWaiter.isPresent()) {
            if(orderDetail.getNote().equals(orderDetailWaiter.get().getNote())){
                Long newOrderDetailWaiterQuantity = orderDetail.getQuantity() + orderDetailWaiter.get().getQuantity();
                BigDecimal newAmountWaiter = orderDetail.getPrice().multiply(BigDecimal.valueOf(newOrderDetailWaiterQuantity));
                orderDetail.setQuantity(newOrderDetailWaiterQuantity);
                orderDetail.setAmount(newAmountWaiter);
                orderDetail.setStatus(EOrderDetailStatus.WAITING);
                orderDetailRepository.save(orderDetail);
                orderDetailRepository.deleteById(orderDetailWaiter.get().getId());
            }
            else {
                orderDetail.setStatus(EOrderDetailStatus.WAITING);
                orderDetailRepository.save(orderDetail);
            }

        }
        else{
            orderDetail.setStatus(EOrderDetailStatus.WAITING);
            orderDetailRepository.save(orderDetail);
        }
    }

    @Override
    public void changeStatusFromCookingToWaitingAllToProductOfOrderOfTable(Order order) {
        String tableName = order.getTableOrder().getTitle();

        List<OrderDetail> orderDetailCookingList  = this.findAllByOrderAndStatus(order, EOrderDetailStatus.COOKING);

        List<OrderDetail> orderDetailWaiterList = this.findAllByOrderAndStatus(order, EOrderDetailStatus.WAITING);

        if(orderDetailCookingList.size() == 0){
            throw new DataInputException(String.format("Hóa đơn '%s' không có danh sách sản phẩm tương ứng trạng thái !!!", tableName));
        }

        if(orderDetailWaiterList.size() == 0) {
            for(OrderDetail item : orderDetailCookingList) {
                item.setStatus(EOrderDetailStatus.WAITING);
            }
            orderDetailRepository.saveAll(orderDetailCookingList);
        }
        else {
            for(OrderDetail orderDetailCooking : orderDetailCookingList) {
                Optional<OrderDetail> orderDetailWaiter = orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(order.getId(), orderDetailCooking.getProduct().getId(), orderDetailCooking.getNote(), EOrderDetailStatus.WAITING);

                if(orderDetailWaiter.isPresent()){
                    Long newOrderDetailQuantity = orderDetailCooking.getQuantity() + orderDetailWaiter.get().getQuantity();
                    orderDetailWaiter.get().setQuantity(newOrderDetailQuantity);
                    BigDecimal newAmountDetailQuantity = orderDetailCooking.getPrice().multiply(BigDecimal.valueOf(newOrderDetailQuantity));
                    orderDetailWaiter.get().setAmount(newAmountDetailQuantity);
                    orderDetailWaiter.get().setStatus(EOrderDetailStatus.WAITING);
                    orderDetailRepository.save(orderDetailWaiter.get());
                    orderDetailRepository.delete(orderDetailCooking);
                }
                else{
                    orderDetailCooking.setStatus(EOrderDetailStatus.WAITING);
                    orderDetailRepository.save(orderDetailCooking);
                }
            }
        }
    }

    @Override
    public OrderDetailKitchenWaiterDTO changeStatusFromCookingToStockOutOfProduct(OrderDetail orderDetailCooking) {
        Order order = orderDetailCooking.getOrder();
        Product product = orderDetailCooking.getProduct();
        String note = orderDetailCooking.getNote();

        Optional<OrderDetail> orderDetailStockOutOptional = this.findByOrderIdAndProductIdAndNoteAndStatusStock(order.getId(), product.getId(), note);

        OrderDetailKitchenWaiterDTO orderItemKitchenWaiterDTO;

        if(!orderDetailStockOutOptional.isPresent()){
            if(orderDetailCooking.getQuantity() == 1){
                orderDetailCooking.setStatus(EOrderDetailStatus.STOCK_OUT);
                orderDetailRepository.save(orderDetailCooking);

                orderItemKitchenWaiterDTO = orderDetailCooking.toOrderItemKitchenWaiterDTO();
            }
            else {
                Long newQuantity = orderDetailCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderDetailCooking.getAmount().multiply(BigDecimal.valueOf(newQuantity));
                orderDetailCooking.setQuantity(newQuantity);
                orderDetailCooking.setAmount(newAmountCooking);
                orderDetailRepository.save(orderDetailCooking);

                OrderDetail newOrderDetail = new OrderDetail()
                        .setOrder(orderDetailCooking.getOrder())
                        .setProduct(orderDetailCooking.getProduct())
                        .setQuantity(1L)
                        .setStatus(EOrderDetailStatus.STOCK_OUT)
                        .setNote(orderDetailCooking.getNote())
                        .setPrice(orderDetailCooking.getPrice())
                        .setAmount(newAmountCooking)
                        ;
                orderDetailRepository.save(newOrderDetail);

                orderItemKitchenWaiterDTO = orderDetailCooking.toOrderItemKitchenWaiterDTO();
            }
        }
        else {
            OrderDetail orderDetailStockOut = orderDetailStockOutOptional.get();

            if(orderDetailCooking.getQuantity() == 1 ){
                orderDetailRepository.deleteById(orderDetailCooking.getId());
            }
            else {
                Long newQuantity = orderDetailCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderDetailCooking.getPrice().multiply(BigDecimal.valueOf(newQuantity));
                orderDetailCooking.setQuantity(newQuantity);
                orderDetailCooking.setAmount(newAmountCooking);
                orderDetailRepository.save(orderDetailCooking);
            }

            Long newQuantityStockOut = orderDetailStockOut.getQuantity() + 1;
            BigDecimal newAmountStockOut = orderDetailStockOut.getPrice().multiply(BigDecimal.valueOf(newQuantityStockOut));
            orderDetailStockOut.setQuantity(newQuantityStockOut);
            orderDetailStockOut.setAmount(newAmountStockOut);
            orderDetailRepository.save(orderDetailStockOut);

            orderItemKitchenWaiterDTO = orderDetailStockOut.toOrderItemKitchenWaiterDTO();
        }



        return orderItemKitchenWaiterDTO;
    }

    @Override
    public void changeStatusFromCookingToStockOutToProduct(OrderDetail orderDetailCooking) {
        Order order = orderDetailCooking.getOrder();
        Product product = orderDetailCooking.getProduct();
        String note = orderDetailCooking.getNote();

        Optional<OrderDetail> orderDetailStockOut = this.findByOrderIdAndProductIdAndNoteAndStatusStock(order.getId(), product.getId(), note);

        if(orderDetailStockOut.isPresent()) {
            Long newOrderDetailStockOutQuantity = orderDetailCooking.getQuantity() + orderDetailStockOut.get().getQuantity();
            BigDecimal newAmountStockout = orderDetailCooking.getPrice().multiply(BigDecimal.valueOf(newOrderDetailStockOutQuantity));
            orderDetailCooking.setQuantity(newOrderDetailStockOutQuantity);
            orderDetailCooking.setAmount(newAmountStockout);
            orderDetailCooking.setStatus(EOrderDetailStatus.STOCK_OUT);
            orderDetailRepository.save(orderDetailCooking);
            orderDetailRepository.deleteById(orderDetailStockOut.get().getId());
        }
        else {
            orderDetailCooking.setStatus(EOrderDetailStatus.STOCK_OUT);
            orderDetailRepository.save(orderDetailCooking);
        }

    }

    @Override
    public List<OrderDetailKitchenGroupDTO> changeStatusFromCookingToStockOutToAllProductsOfGroup(Long productId, String note) {
        List<OrderDetail> orderDetailsCooking = orderDetailRepository.findAllByProductIdAndNoteAndStatus(productId, note, EOrderDetailStatus.COOKING);

        if(orderDetailsCooking.size() == 0){
            throw new DataInputException("Không tìm thấy hóa đơn theo sản phẩm với trạng thái tương ứng !!!");
        }

        List<OrderDetailKitchenGroupDTO> orderDetailKitchenGroupDTOS = new ArrayList<>();

        for(OrderDetail orderDetailCooking : orderDetailsCooking) {
            Optional<OrderDetail> orderDetailStockOutOptional = this.findByOrderIdAndProductIdAndNoteAndStatusStock(orderDetailCooking.getOrder().getId(), productId, note);

            if(!orderDetailStockOutOptional.isPresent()) {
                orderDetailCooking.setStatus(EOrderDetailStatus.STOCK_OUT);
                orderDetailRepository.save(orderDetailCooking);

                OrderDetailKitchenGroupDTO item = orderDetailCooking.toOrderDetailKitchenGroupDTO();
                orderDetailKitchenGroupDTOS.add(item);
            }
            else {
                OrderDetail orderDetailStockOut = orderDetailStockOutOptional.get();

                Long newQuantityStockOut = orderDetailStockOut.getQuantity() + orderDetailCooking.getQuantity();
                BigDecimal newAmountStockOut  = orderDetailStockOut.getPrice().multiply(BigDecimal.valueOf(newQuantityStockOut));
                orderDetailStockOut.setQuantity(newQuantityStockOut);
                orderDetailStockOut.setAmount(newAmountStockOut);
                orderDetailRepository.save(orderDetailStockOut);

                OrderDetailKitchenGroupDTO item = orderDetailStockOut.toOrderDetailKitchenGroupDTO();
                orderDetailKitchenGroupDTOS.add(item);

                orderDetailRepository.delete(orderDetailCooking);
            }
        }
        return orderDetailKitchenGroupDTOS;
    }

    @Override
    public OrderDetailKitchenWaiterDTO changeStatusFromCookingToWaiterToOneProductOfGroup(Long productId, String note) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByProductIdAndNoteAndStatusOrderByIdAsc(productId, note, EOrderDetailStatus.COOKING);

        if(orderDetails.size() == 0) {
            throw new DataInputException("Không tìm thấy hóa đơn theo sản phẩm với trạng thái tương ứng !!!");
        }

        OrderDetail orderDetailCooking = orderDetails.get(0);

        Optional<OrderDetail> orderDetailWaiterOptional = this.findByOrderIdAndProductIdAndNoteAndStatusWaiting(orderDetailCooking.getOrder().getId(), productId, note );

        OrderDetailKitchenWaiterDTO orderDetailKitchenWaiterDTO;

        if(!orderDetailWaiterOptional.isPresent()) {
            if(orderDetailCooking.getQuantity() == 1){
                orderDetailCooking.setStatus(EOrderDetailStatus.WAITING);
                orderDetailRepository.save(orderDetailCooking);

                orderDetailKitchenWaiterDTO = orderDetailCooking.toOrderItemKitchenWaiterDTO();
            }
            else {
                Long newQuantity = orderDetailCooking.getQuantity() - 1 ;
                BigDecimal newAmountCooking = orderDetailCooking.getPrice().multiply(BigDecimal.valueOf(newQuantity));
                orderDetailCooking.setQuantity(newQuantity);
                orderDetailCooking.setAmount(newAmountCooking);
                orderDetailRepository.save(orderDetailCooking);

                OrderDetail newOrderDetail = new OrderDetail()
                        .setOrder(orderDetailCooking.getOrder())
                        .setProduct(orderDetailCooking.getProduct())
                        .setQuantity(1L)
                        .setStatus(EOrderDetailStatus.WAITING)
                        .setNote(orderDetailCooking.getNote())
                        .setPrice(orderDetailCooking.getPrice())
                        .setAmount(newAmountCooking)
                        ;
                orderDetailRepository.save(newOrderDetail);

                orderDetailKitchenWaiterDTO = newOrderDetail.toOrderItemKitchenWaiterDTO();
            }
        }
        else {
            OrderDetail orderDetailWaiter = orderDetailWaiterOptional.get();

            if(orderDetailCooking.getQuantity() == 1){
                orderDetailRepository.deleteById(orderDetailCooking.getId());
            }
            else {
                Long newQuantity = orderDetailCooking.getQuantity() - 1;
                BigDecimal newAmountCooking = orderDetailCooking.getPrice().multiply(BigDecimal.valueOf(newQuantity));
                orderDetailCooking.setQuantity(newQuantity);
                orderDetailCooking.setAmount(newAmountCooking);
                orderDetailRepository.save(orderDetailCooking);

            }
            Long newQuantityWaiter = orderDetailWaiter.getQuantity() + 1;
            BigDecimal  newAmountWaiter = orderDetailCooking.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
            orderDetailWaiter.setQuantity(newQuantityWaiter);
            orderDetailWaiter.setAmount(newAmountWaiter);
            orderDetailRepository.save(orderDetailWaiter);

            orderDetailKitchenWaiterDTO = orderDetailWaiter.toOrderItemKitchenWaiterDTO();
        }
        return orderDetailKitchenWaiterDTO;
    }

    @Override
    public List<OrderDetailKitchenWaiterDTO> changeStatusFromCookingToWaiterToAllProductsOfGroup(Long productId, String note) {
        List<OrderDetail> orderDetailsCooking = orderDetailRepository.findAllByProductIdAndNoteAndStatus(productId, note, EOrderDetailStatus.COOKING);

        if(orderDetailsCooking.size() == 0){
            throw new DataInputException("Không tìm thấy hóa đơn theo sản phẩm với trạng thái tương ứng !!!");
        }

        List<OrderDetailKitchenWaiterDTO> orderDetailKitchenWaiterDTOList = new ArrayList<>();

        for(OrderDetail orderDetailCooking : orderDetailsCooking) {
            Optional<OrderDetail> orderDetailWaiterOptional = this.findByOrderIdAndProductIdAndNoteAndStatusWaiting(orderDetailCooking.getOrder().getId(), productId, note);

            if(!orderDetailWaiterOptional.isPresent()) {
                orderDetailCooking.setStatus(EOrderDetailStatus.WAITING);
                orderDetailRepository.save(orderDetailCooking);

                OrderDetailKitchenWaiterDTO item = orderDetailCooking.toOrderItemKitchenWaiterDTO();
                orderDetailKitchenWaiterDTOList.add(item);
            }
            else {
                OrderDetail orderDetailWaiter = orderDetailWaiterOptional.get();

                Long newQuantityWaiter = orderDetailWaiter.getQuantity() + orderDetailCooking.getQuantity();
                BigDecimal newAmountWaiter  = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailRepository.save(orderDetailWaiter);

                OrderDetailKitchenWaiterDTO item = orderDetailWaiter.toOrderItemKitchenWaiterDTO();
                orderDetailKitchenWaiterDTOList.add(item);

                orderDetailRepository.delete(orderDetailCooking);
            }
        }
        return orderDetailKitchenWaiterDTOList;
    }

    @Override
    public void changeStatusFromWaiterToDeliveryOfProduct(OrderDetail orderDetailWaiter) {
        Order order = orderDetailWaiter.getOrder();
        Product product = orderDetailWaiter.getProduct();
        String note = orderDetailWaiter.getNote();

        Optional<OrderDetail> orderDetailDeliveryOptional = this.findByOrderIdAndProductIdAndNoteAndStatusDelivery(order.getId(), product.getId(), note);
        if (!orderDetailDeliveryOptional.isPresent()) {
            if (orderDetailWaiter.getQuantity() == 1) {
                orderDetailWaiter.setStatus(EOrderDetailStatus.DELIVERY);
                orderDetailRepository.save(orderDetailWaiter);
            }
            else {
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailRepository.save(orderDetailWaiter);

                OrderDetail newOrderItem = new OrderDetail()
                        .setOrder(orderDetailWaiter.getOrder())
                        .setProduct(orderDetailWaiter.getProduct())
                        .setStatus(EOrderDetailStatus.DELIVERY)
                        .setNote(orderDetailWaiter.getNote())
                        .setPrice(orderDetailWaiter.getPrice())
                        .setQuantity(1L)
                        .setAmount(orderDetailWaiter.getPrice())
                        ;
                orderDetailRepository.save(newOrderItem);
            }
        }
        else {
            OrderDetail orderDetailDelivery = orderDetailDeliveryOptional.get();

            if (orderDetailWaiter.getQuantity() == 1) {
                orderDetailRepository.deleteById(orderDetailWaiter.getId());
            }
            else {
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailRepository.save(orderDetailWaiter);
            }

            Long newQuantityDelivery = orderDetailDelivery.getQuantity() + 1;
            BigDecimal newAmountDelivery = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
            orderDetailDelivery.setQuantity(newQuantityDelivery);
            orderDetailDelivery.setAmount(newAmountDelivery);
            orderDetailRepository.save(orderDetailDelivery);
        }

    }

    @Override
    public void changeStatusFromWaiterToDeliveryToProductOfOrder(OrderDetail orderDetailWaiter) {
        Order order = orderDetailWaiter.getOrder();
        Product product = orderDetailWaiter.getProduct();
        String note = orderDetailWaiter.getNote();

        Optional<OrderDetail> orderDetailDelivery = this.findByOrderIdAndProductIdAndNoteAndStatusDelivery(order.getId(), product.getId(), note);

        if(orderDetailDelivery.isPresent()){
            if(orderDetailWaiter.getNote().equals(orderDetailDelivery.get().getNote())){
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() + orderDetailDelivery.get().getQuantity();
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailWaiter.setStatus(EOrderDetailStatus.DELIVERY);
                orderDetailRepository.save(orderDetailWaiter);
                orderDetailRepository.deleteById(orderDetailDelivery.get().getId());
            }
            else {
                orderDetailWaiter.setStatus(EOrderDetailStatus.DELIVERY);
                orderDetailRepository.save(orderDetailWaiter);
            }
        }
        else {
            orderDetailWaiter.setStatus(EOrderDetailStatus.DELIVERY);
            orderDetailRepository.save(orderDetailWaiter);
        }

    }

    @Override
    public void changeStatusFromWaiterToDeliveryAllProductOfTable(Order order) {
        String tableName = order.getTableOrder().getTitle();

        List<OrderDetail> orderDetailWaiterList = this.findAllByOrderAndStatus(order, EOrderDetailStatus.WAITING);

        List<OrderDetail> orderDetailDeliveryList = this.findAllByOrderAndStatus(order, EOrderDetailStatus.DELIVERY);

        if(orderDetailWaiterList.size() == 0) {
            throw new DataInputException(String.format("Hóa đơn '%s' không có danh sách sản phẩm tương tứng trạng thái", tableName));
        }

        if(orderDetailDeliveryList.size() == 0) {
            for (OrderDetail item : orderDetailWaiterList) {
                item.setStatus(EOrderDetailStatus.DELIVERY);
            }
            orderDetailRepository.saveAll(orderDetailWaiterList);
        }
        else {
            for(OrderDetail orderDetailWaiter : orderDetailWaiterList) {
                Optional<OrderDetail> orderDetailDelivery = orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(order.getId(), orderDetailWaiter.getProduct().getId(), orderDetailWaiter.getNote(), EOrderDetailStatus.DELIVERY);

                if(orderDetailDelivery.isPresent()){
                    Long newQuantity = orderDetailWaiter.getQuantity() + orderDetailDelivery.get().getQuantity();
                    orderDetailDelivery.get().setQuantity(newQuantity);
                    BigDecimal newAmount = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantity));
                    orderDetailDelivery.get().setAmount(newAmount);
                    orderDetailDelivery.get().setStatus(EOrderDetailStatus.DELIVERY);
                    orderDetailRepository.save(orderDetailDelivery.get());
                    orderDetailRepository.delete(orderDetailWaiter);
                }
                else {
                    orderDetailWaiter.setStatus(EOrderDetailStatus.DELIVERY);
                    orderDetailRepository.save(orderDetailWaiter);
                }
            }
        }

    }

    @Override
    public void changeStatusFromWaiterToDoneOfProduct(OrderDetail orderDetailWaiter) {
        Order order = orderDetailWaiter.getOrder();
        Product product = orderDetailWaiter.getProduct();
        String note = orderDetailWaiter.getNote();

        Optional<OrderDetail> orderDetailDoneOptional = this.findByOrderIdAndProductIdAndNoteAndStatusDone(order.getId(), product.getId(), note);
        if (!orderDetailDoneOptional.isPresent()) {
            if (orderDetailWaiter.getQuantity() == 1) {
                orderDetailWaiter.setStatus(EOrderDetailStatus.DONE);
                orderDetailRepository.save(orderDetailWaiter);
            }
            else {
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailRepository.save(orderDetailWaiter);

                OrderDetail newOrderItem = new OrderDetail()
                        .setOrder(orderDetailWaiter.getOrder())
                        .setProduct(orderDetailWaiter.getProduct())
                        .setStatus(EOrderDetailStatus.DONE)
                        .setNote(orderDetailWaiter.getNote())
                        .setPrice(orderDetailWaiter.getPrice())
                        .setQuantity(1L)
                        .setAmount(orderDetailWaiter.getPrice())
                        ;
                orderDetailRepository.save(newOrderItem);
            }
        }
        else {
            OrderDetail orderDetailDone = orderDetailDoneOptional.get();

            if (orderDetailWaiter.getQuantity() == 1) {
                orderDetailRepository.deleteById(orderDetailWaiter.getId());
            }
            else {
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailRepository.save(orderDetailWaiter);
            }

            Long newQuantityDone = orderDetailDone.getQuantity() + 1;
            BigDecimal newAmountDone = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityDone));
            orderDetailDone.setQuantity(newQuantityDone);
            orderDetailDone.setAmount(newAmountDone);
            orderDetailRepository.save(orderDetailDone);
        }
    }

    @Override
    public void changeStatusFromWaiterToDoneToProductOfOrder(OrderDetail orderDetailWaiter) {
        Order order = orderDetailWaiter.getOrder();
        Product product = orderDetailWaiter.getProduct();
        String note = orderDetailWaiter.getNote();

        Optional<OrderDetail> orderDetailDone = this.findByOrderIdAndProductIdAndNoteAndStatusDone(order.getId(), product.getId(), note);

        if(orderDetailDone.isPresent()){
            if(orderDetailWaiter.getNote().equals(orderDetailDone.get().getNote())){
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() + orderDetailDone.get().getQuantity();
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailWaiter.setStatus(EOrderDetailStatus.DONE);
                orderDetailRepository.save(orderDetailWaiter);
                orderDetailRepository.deleteById(orderDetailDone.get().getId());
            }
            else {
                orderDetailWaiter.setStatus(EOrderDetailStatus.DONE);
                orderDetailRepository.save(orderDetailWaiter);
            }
        }
        else {
            orderDetailWaiter.setStatus(EOrderDetailStatus.DONE);
            orderDetailRepository.save(orderDetailWaiter);
        }
    }

    @Override
    public void changeStatusFromWaiterToDoneAllProductOfTable(Order order) {
        String tableName = order.getTableOrder().getTitle();

        List<OrderDetail> orderDetailWaiterList = this.findAllByOrderAndStatus(order, EOrderDetailStatus.WAITING);

        List<OrderDetail> orderDetailDoneList = this.findAllByOrderAndStatus(order, EOrderDetailStatus.DONE);

        if(orderDetailWaiterList.size() == 0) {
            throw new DataInputException(String.format("Hóa đơn '%s' không có danh sách sản phẩm tương tứng trạng thái", tableName));
        }

        if(orderDetailDoneList.size() == 0) {
            for (OrderDetail item : orderDetailWaiterList) {
                item.setStatus(EOrderDetailStatus.DONE);
            }
            orderDetailRepository.saveAll(orderDetailWaiterList);
        }
        else {
            for(OrderDetail orderDetailWaiter : orderDetailWaiterList) {
                Optional<OrderDetail> orderDetailDone = orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(order.getId(), orderDetailWaiter.getProduct().getId(), orderDetailWaiter.getNote(), EOrderDetailStatus.DONE);

                if(orderDetailDone.isPresent()){
                    Long newQuantity = orderDetailWaiter.getQuantity() + orderDetailDone.get().getQuantity();
                    orderDetailDone.get().setQuantity(newQuantity);
                    BigDecimal newAmount = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantity));
                    orderDetailDone.get().setAmount(newAmount);
                    orderDetailDone.get().setStatus(EOrderDetailStatus.DONE);
                    orderDetailRepository.save(orderDetailDone.get());
                    orderDetailRepository.delete(orderDetailWaiter);
                }
                else {
                    orderDetailWaiter.setStatus(EOrderDetailStatus.DONE);
                    orderDetailRepository.save(orderDetailWaiter);
                }
            }
        }
    }

    @Override
    public void changeStatusFromWaiterToStockOutOfProduct(OrderDetail orderDetailWaiter) {
        Order order = orderDetailWaiter.getOrder();
        Product product = orderDetailWaiter.getProduct();
        String note = orderDetailWaiter.getNote();

        Optional<OrderDetail> orderDetailStockOutOptional = this.findByOrderIdAndProductIdAndNoteAndStatusStock(order.getId(), product.getId(), note);

        if (!orderDetailStockOutOptional.isPresent()) {
            if (orderDetailWaiter.getQuantity() == 1) {
                orderDetailWaiter.setStatus(EOrderDetailStatus.STOCK_OUT);
                orderDetailRepository.save(orderDetailWaiter);
            }
            else {
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailRepository.save(orderDetailWaiter);

                OrderDetail newOrderItem = new OrderDetail()
                        .setOrder(orderDetailWaiter.getOrder())
                        .setProduct(orderDetailWaiter.getProduct())
                        .setStatus(EOrderDetailStatus.STOCK_OUT)
                        .setNote(orderDetailWaiter.getNote())
                        .setPrice(orderDetailWaiter.getPrice())
                        .setQuantity(1L)
                        .setAmount(orderDetailWaiter.getPrice())
                        ;
                orderDetailRepository.save(newOrderItem);
            }
        }
        else {
            OrderDetail orderDetailDelivery = orderDetailStockOutOptional.get();

            if (orderDetailWaiter.getQuantity() == 1) {
                orderDetailRepository.deleteById(orderDetailWaiter.getId());
            }
            else {
                Long newQuantityWaiter = orderDetailWaiter.getQuantity() - 1;
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityWaiter));
                orderDetailWaiter.setQuantity(newQuantityWaiter);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailRepository.save(orderDetailWaiter);
            }

            Long newQuantityDelivery = orderDetailDelivery.getQuantity() + 1;
            BigDecimal newAmountDelivery = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
            orderDetailDelivery.setQuantity(newQuantityDelivery);
            orderDetailDelivery.setAmount(newAmountDelivery);
            orderDetailRepository.save(orderDetailDelivery);
        }


    }

    @Override
    public void changeStatusFromWaiterToStockOutToProductOfOrder(OrderDetail orderDetailWaiter) {
        Order order = orderDetailWaiter.getOrder();
        Product product = orderDetailWaiter.getProduct();
        String note = orderDetailWaiter.getNote();

        Optional<OrderDetail> orderDetailDelivery = this.findByOrderIdAndProductIdAndNoteAndStatusStock(order.getId(), product.getId(), note);

        if (orderDetailDelivery.isPresent()) {
            if (orderDetailWaiter.getNote().equals(orderDetailDelivery.get().getNote())) {
                Long newOrderItemWaiterQuantity = orderDetailWaiter.getQuantity() + orderDetailDelivery.get().getQuantity();
                BigDecimal newAmountWaiter = orderDetailWaiter.getPrice().multiply(BigDecimal.valueOf(newOrderItemWaiterQuantity));
                orderDetailWaiter.setQuantity(newOrderItemWaiterQuantity);
                orderDetailWaiter.setAmount(newAmountWaiter);
                orderDetailWaiter.setStatus(EOrderDetailStatus.STOCK_OUT);
                orderDetailRepository.save(orderDetailWaiter);
                orderDetailRepository.deleteById(orderDetailDelivery.get().getId());
            }
            else {
                orderDetailWaiter.setStatus(EOrderDetailStatus.STOCK_OUT);
                orderDetailRepository.save(orderDetailWaiter);
            }
        }
        else {
            orderDetailWaiter.setStatus(EOrderDetailStatus.STOCK_OUT);
            orderDetailRepository.save(orderDetailWaiter);
        }

    }

    @Override
    public void changeStatusFromDeliveryToDoneOfProduct(OrderDetail orderDetailDelivery) {
        Order order = orderDetailDelivery.getOrder();
        Product product = orderDetailDelivery.getProduct();
        String note = orderDetailDelivery.getNote();

        Optional<OrderDetail> orderDetailDoneOptional = this.findByOrderIdAndProductIdAndNoteAndStatusDone(order.getId(), product.getId(), note);

        if (!orderDetailDoneOptional.isPresent()) {
            if (orderDetailDelivery.getQuantity() == 1) {
                orderDetailDelivery.setStatus(EOrderDetailStatus.DONE);
                orderDetailRepository.save(orderDetailDelivery);
            }
            else {
                Long newQuantityDelivery = orderDetailDelivery.getQuantity() - 1;
                BigDecimal newAmountDelivery = orderDetailDelivery.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
                orderDetailDelivery.setQuantity(newQuantityDelivery);
                orderDetailDelivery.setAmount(newAmountDelivery);
                orderDetailRepository.save(orderDetailDelivery);

                OrderDetail newOrderItem = new OrderDetail()
                        .setOrder(orderDetailDelivery.getOrder())
                        .setProduct(orderDetailDelivery.getProduct())
                        .setStatus(EOrderDetailStatus.DONE)
                        .setNote(orderDetailDelivery.getNote())
                        .setPrice(orderDetailDelivery.getPrice())
                        .setQuantity(1L)
                        .setAmount(orderDetailDelivery.getPrice())
                        ;
                orderDetailRepository.save(newOrderItem);
            }
        }
        else {
            OrderDetail orderDetailDone = orderDetailDoneOptional.get();

            if (orderDetailDelivery.getQuantity() == 1) {
                orderDetailRepository.deleteById(orderDetailDelivery.getId());
            }
            else {
                Long newQuantityDelivery = orderDetailDelivery.getQuantity() - 1;
                BigDecimal newAmountDelivery = orderDetailDelivery.getPrice().multiply(BigDecimal.valueOf(newQuantityDelivery));
                orderDetailDelivery.setQuantity(newQuantityDelivery);
                orderDetailDelivery.setAmount(newAmountDelivery);
                orderDetailRepository.save(orderDetailDelivery);
            }

            Long newQuantityDone = orderDetailDone.getQuantity() + 1;
            BigDecimal newAmountDone = orderDetailDelivery.getPrice().multiply(BigDecimal.valueOf(newQuantityDone));
            orderDetailDone.setQuantity(newQuantityDone);
            orderDetailDone.setAmount(newAmountDone);
            orderDetailRepository.save(orderDetailDone);
        }
    }

    @Override
    public void changeStatusFromDeliveryToDoneToProductOfOrder(OrderDetail orderDetailDelivery) {
        Order order = orderDetailDelivery.getOrder();
        Product product = orderDetailDelivery.getProduct();
        String note = orderDetailDelivery.getNote();

        Optional<OrderDetail> orderDetailDone = this.findByOrderIdAndProductIdAndNoteAndStatusDone(order.getId(), product.getId(), note);

        if (orderDetailDone.isPresent()) {
            if (orderDetailDelivery.getNote().equals(orderDetailDone.get().getNote())) {
                Long newOrderItemDeliveryQuantity = orderDetailDelivery.getQuantity() + orderDetailDone.get().getQuantity();
                BigDecimal newAmountDelivery = orderDetailDelivery.getPrice().multiply(BigDecimal.valueOf(newOrderItemDeliveryQuantity));
                orderDetailDelivery.setQuantity(newOrderItemDeliveryQuantity);
                orderDetailDelivery.setAmount(newAmountDelivery);
                orderDetailDelivery.setStatus(EOrderDetailStatus.DONE);
                orderDetailRepository.save(orderDetailDelivery);
                orderDetailRepository.deleteById(orderDetailDone.get().getId());
            }
            else {
                orderDetailDelivery.setStatus(EOrderDetailStatus.DONE);
                orderDetailRepository.save(orderDetailDelivery);
            }
        }
        else {
            orderDetailDelivery.setStatus(EOrderDetailStatus.DONE);
            orderDetailRepository.save(orderDetailDelivery);
        }

    }

    @Override
    public void changeStatusFromDeliveryToDoneToTableAll(Order order) {
        String tableName = order.getTableOrder().getTitle();

        List<OrderDetail> orderDetailDeliveryList = this.findAllByOrderAndStatus(order, EOrderDetailStatus.DELIVERY);

        List<OrderDetail> orderDetailDoneList = this.findAllByOrderAndStatus(order, EOrderDetailStatus.DONE);

        if(orderDetailDeliveryList.size() == 0) {
            throw new DataInputException(String.format("Hóa đơn '%s' không có danh sách sản phẩm tương tứng trạng thái", tableName));
        }

        if(orderDetailDoneList.size() == 0) {
            for (OrderDetail item : orderDetailDeliveryList) {
                item.setStatus(EOrderDetailStatus.DONE);
            }
            orderDetailRepository.saveAll(orderDetailDeliveryList);
        }
        else {
            for (OrderDetail orderDetailDelivery : orderDetailDeliveryList) {
                Optional<OrderDetail> orderDetailDone = orderDetailRepository.findByOrderIdAndProductIdAndNoteAndOrderDetailStatus(order.getId(), orderDetailDelivery.getProduct().getId(), orderDetailDelivery.getNote(), EOrderDetailStatus.DONE);

                if (orderDetailDone.isPresent()) {
                    Long newOrderItemQuantity = orderDetailDelivery.getQuantity() + orderDetailDone.get().getQuantity();
                    orderDetailDone.get().setQuantity(newOrderItemQuantity);
                    BigDecimal newAmountItemQuantity = orderDetailDelivery.getPrice().multiply(BigDecimal.valueOf(newOrderItemQuantity));
                    orderDetailDone.get().setAmount(newAmountItemQuantity);
                    orderDetailDone.get().setStatus(EOrderDetailStatus.DONE);
                    orderDetailRepository.save(orderDetailDone.get());
                    orderDetailRepository.delete(orderDetailDelivery);
                }
                else {
                    orderDetailDelivery.setStatus(EOrderDetailStatus.DONE);
                    orderDetailRepository.save(orderDetailDelivery);
                }
            }
        }

    }

    @Override
    public void deleteOrderDetailStockOut(OrderDetail orderDetail) {
        Order order = orderDetail.getOrder();

        orderDetailRepository.delete(orderDetail);

        //tính lại tổng giá tiền của order
        BigDecimal newTotalAmount = orderService.getOrderTotalAmount(order.getId());
        order.setTotalAmount(newTotalAmount);
        orderRepository.save(order);

    }

    @Override
    public List<OrderDetail> getAllByOrder(Order order) {
        return orderDetailRepository.getAllByOrder(order);
    }

    @Override
    public List<BillPrintItemDTO> getAllBillPrintItemDTOByOrderId(Long orderId) {
        return orderDetailRepository.getAllBillPrintItemDTOByOrderId(orderId);
    }

    @Override
    public List<OrderDetailDTO> getOrderItemDTOByOrderId(Long orderId) {
        return orderDetailRepository.getOrderItemDTOByOrderId(orderId);
    }


}
