package com.cg.api;

import com.cg.domain.dto.order.OrderKitchenTableDTO;
import com.cg.domain.dto.orderDetail.IOrderDetailKitchenGroupDTO;
import com.cg.domain.dto.orderDetail.IOrderDetailKitchenWaiterDTO;
import com.cg.domain.dto.orderDetail.OrderDetailKitchenGroupDTO;
import com.cg.domain.dto.orderDetail.OrderDetailKitchenTableDTO;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.service.order.IOrderService;
import com.cg.service.orderDetail.IOrderDetailService;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
