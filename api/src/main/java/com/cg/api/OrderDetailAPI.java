package com.cg.api;

import com.cg.domain.dto.order.OrderKitchenTableDTO;
import com.cg.domain.dto.orderDetail.*;
import com.cg.domain.entity.Order;
import com.cg.domain.entity.OrderDetail;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.exception.DataInputException;
import com.cg.service.order.IOrderService;
import com.cg.service.orderDetail.IOrderDetailService;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/order-details")
public class OrderDetailAPI {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private ValidateUtils validateUtils;


    @GetMapping("/kitchen/get-all")
    public ResponseEntity<?> getAll() {
        Map<String, List<?>> result = getAllItem();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    public Map<String, List<?>> getAllItem() {
        List<IOrderDetailKitchenGroupDTO> items = orderDetailService.getOrderDetailByStatusCookingGroupByProduct();

        List<OrderKitchenTableDTO> itemsTable = orderService.getAllOrderKitchenCookingByTable(EOrderDetailStatus.COOKING);

        List<IOrderDetailKitchenWaiterDTO> itemsWaiter = orderDetailService.getOrderDetailByStatusWaiterGroupByTableAndProduct();

        Map<String, List<?>> result = new HashMap<>();

        result.put("itemsCooking", items);
        result.put("itemsTable", itemsTable);
        result.put("itemsWaiter", itemsWaiter);

        return result;
    }

    //get-all-order-detail-status-cooking-group-by-product-for-kitchen
    @GetMapping("/kitchen/get-by-status-cooking")
    public ResponseEntity<?> getByStatusCooking() {
        List<OrderDetailKitchenGroupDTO> orderDetailList = orderDetailService.getOrderItemByStatusGroupByProduct(EOrderDetailStatus.COOKING);

        if(orderDetailList.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderDetailList, HttpStatus.OK);
    }

    //get-all-order-detail-status-cooking-group-by-table-for-kitchen
    @GetMapping("/kitchen/get-by-status-cooking-group-by-table")
    public ResponseEntity<?> getBystatusCookingGroupByTable() {
        List<OrderKitchenTableDTO> orderList = orderService.getAllOrderKitchenCookingByTable(EOrderDetailStatus.COOKING);

        if(orderList.size() == 0){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderList, HttpStatus.OK);


    }

    //change-status-one-product-of-table-from-cooking-to-waiting
    @PostMapping("/kitchen/table/change-status-cooking-to-waiting")
    public ResponseEntity<?> changeStatusFromCookingToWaiterOfProduct(@RequestParam("orderDetailId") String orderDetailStr){
        if(!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hoá đơn phải là ký tự !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hoá đơn không tồn tại !!!");
        });

        if(!orderDetail.getStatus().equals(EOrderDetailStatus.COOKING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hoá đơn '%s' không có sản phẩm tương ứng trạng thái !!!",tableName));
        }

        Order order = orderDetail.getOrder();

        if(order.getPaid()){
            throw new DataInputException("Hoá đơn này đã được thanh toán !!!");
        }

        OrderDetailKitchenWaiterDTO orderDetailKitchenWaiterDTO = orderDetailService.changeStatusFromCookingToWaiterOfProduct(orderDetail);

        return new ResponseEntity<>(orderDetailKitchenWaiterDTO, HttpStatus.OK);
    }

}
