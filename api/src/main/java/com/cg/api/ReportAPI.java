package com.cg.api;

import com.cg.domain.dto.product.IProductReportDTO;
import com.cg.domain.dto.product.ProductCountDTO;
import com.cg.domain.dto.report.*;
import com.cg.domain.dto.staff.StaffCountDTO;
import com.cg.domain.dto.tableOrder.TableOrderCountDTO;
import com.cg.service.bill.IBillService;
import com.cg.service.order.IOrderService;
import com.cg.service.orderDetail.IOrderDetailService;
import com.cg.service.product.IProductService;
import com.cg.service.staff.IStaffService;
import com.cg.service.tableOrder.ITableOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    private IBillService iBillService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        IReportDTO dayReport = iBillService.getReportOfCurrentDay();
        IReportDTO monthReport = iBillService.getReportOfCurrentMonth();
        List<IYearReportDTO> yearReport = iBillService.getReportByCurrentYear();
        List<I6MonthAgoReportDTO> sixMonthAgoReport = iBillService.getReport6MonthAgo();
        List<IDayToDayReportDTO> tenDaysAgoReport = iBillService.getReportFrom10DaysAgo();
        List<IProductReportDTO> top5BestSellReport = iOrderDetailService.getTop5BestSellCurrentMonth();
        ProductCountDTO countProduct = iProductService.countProduct();
        StaffCountDTO countStaff = iStaffService.countStaff();
        TableOrderCountDTO countTableOrder = iTableOrderService.countTable();

        AllReportDTO allReportDTO = new AllReportDTO();
        allReportDTO.setDayReport(dayReport);
        allReportDTO.setMonthReport(monthReport);
        allReportDTO.setYearReport(yearReport);
        allReportDTO.setTop5BestSellReport(top5BestSellReport);
        allReportDTO.setTenDaysAgoReport(tenDaysAgoReport);
        allReportDTO.setCountProduct(countProduct.getCountProduct());
        allReportDTO.setCountStaff(countStaff.getCountStaff());
        allReportDTO.setCountTable(countTableOrder.getCountTableOrder());
        allReportDTO.setSixMonthAgoReport(sixMonthAgoReport);

        return new ResponseEntity<>(allReportDTO, HttpStatus.OK);

    }


}
