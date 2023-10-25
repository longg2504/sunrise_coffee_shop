package com.cg.api;

import com.cg.domain.dto.orderDetail.OrderDetailKitchenGroupDTO;
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

import java.util.List;

@RestController
@RequestMapping("api/order-details")
public class OrderDetailAPI {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private ValidateUtils validateUtils;

    @GetMapping()
    public ResponseEntity<?> getAllItem() {
        List<OrderDetailKitchenGroupDTO> orderDetailList = orderDetailService.getOrderItemByStatusGroupByProduct(EOrderDetailStatus.WAITER);
        if(orderDetailList.size() == 0) {
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return  new ResponseEntity<>(orderDetailList, HttpStatus.OK);
    }

}
