package com.cg.api;

import com.cg.service.order.IOrderService;
import com.cg.service.orderDetail.IOrderDetailService;
import com.cg.service.product.IProductService;
import com.cg.service.staff.IStaffService;
import com.cg.service.tableOrder.ITableOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportAPI {
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IOrderDetailService iOrderDetailService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IStaffService iStaffService;

    @Autowired
    private ITableOrderService iTableOrderService;



}
