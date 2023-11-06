package com.cg.api;

import com.cg.domain.dto.product.IProductReportDTO;
import com.cg.domain.dto.product.ProductCountDTO;
import com.cg.domain.dto.report.*;
import com.cg.domain.dto.staff.StaffCountDTO;
import com.cg.domain.dto.tableOrder.TableOrderCountDTO;
import com.cg.exception.DataInputException;
import com.cg.service.bill.IBillService;
import com.cg.service.order.IOrderService;
import com.cg.service.orderDetail.IOrderDetailService;
import com.cg.service.product.IProductService;
import com.cg.service.staff.IStaffService;
import com.cg.service.tableOrder.ITableOrderService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping("/day/{day}")
    public ResponseEntity<?> getReportDay(@PathVariable String day){
        ReportDTO reportDTO = iBillService.getReportOfDay(day);

        if(reportDTO == null){
            throw new DataInputException("Ngày " + day + " chưa có doanh thu!!!");
        }

        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

    @GetMapping("/current-day")
    public ResponseEntity<?> getReportOfCurrentDay() {
        IReportDTO reportDTO = iBillService.getReportOfCurrentDay();

        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

    @GetMapping("/current-month")
    public ResponseEntity<?> getReportOfCurrentMonth() {
        IReportDTO reportDTO = iBillService.getReportOfCurrentMonth();

        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }

    @GetMapping("/current-year")
    public ResponseEntity<?> getReportOfCurrentYear(){
        List<IYearReportDTO> reportDTO = iBillService.getReportByCurrentYear();

        return new ResponseEntity<>(reportDTO,HttpStatus.OK);
    }


    @GetMapping("/year/{year}")
    public ResponseEntity<?> getReportByyear(@PathVariable int year){
        List<YearReportDTO> reportDTOS = iBillService.getReportByYear(year);

        if(reportDTOS.size() == 0 ){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }

    @GetMapping("/month/{year}-{month}")
    public ResponseEntity<?> getReportByMonth(@PathVariable int year, @PathVariable int month) {

        YearReportDTO reportMonth = iBillService.getReportByMonth(month, year);

        if (reportMonth == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportMonth, HttpStatus.OK);
    }

    @GetMapping("/10-days-ago")
    public ResponseEntity<?> getReportFrom10DaysAgo() {
        List<IDayToDayReportDTO> list = iBillService.getReportFrom10DaysAgo();

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping("/day/{startDay}/{endDay}")
    public ResponseEntity<?> getReportFromDayToDay(@PathVariable String startDay, @PathVariable String endDay) {

        String[] startDayArray = startDay.split("-");
        String[] endDayArray = endDay.split("-");

        int startDayTemp = Integer.parseInt(startDayArray[startDayArray.length - 1]) - 1;
        if (startDayTemp < 10)
            startDayArray[startDayArray.length - 1] = "0" + startDayTemp;
        else
            startDayArray[startDayArray.length - 1] = String.valueOf(startDayTemp);

        startDay = String.join("-", startDayArray);

        int endDayTemp = Integer.parseInt(endDayArray[endDayArray.length - 1]) + 1;
        if (endDayTemp < 10)
            endDayArray[endDayArray.length - 1] = "0" + endDayTemp;
        else
            endDayArray[endDayArray.length - 1] = String.valueOf(endDayTemp);

        endDay = String.join("-", endDayArray);

        List<DayToDayReportDTO> report = iBillService.getReportFromDayToDay(startDay, endDay);

        if (report.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(report, HttpStatus.OK);
    }



    @GetMapping("/product/top5-best-sell")
    public ResponseEntity<?> getTop5ProductBestSellCurrentMonth() {
        List<IProductReportDTO> reportProductDTOS = iOrderDetailService.getTop5BestSellCurrentMonth();

        return new ResponseEntity<>(reportProductDTOS, HttpStatus.OK);
    }

    @GetMapping("/product/top5/{year}-{month}-{sort}")

    public ResponseEntity<?> getTop5Product(@PathVariable int year, @PathVariable int month, Pageable pageable, @PathVariable String sort) {
        List<ProductReportDTO> reportProductDTOS = new ArrayList<ProductReportDTO>();

//        if (sort.equalsIgnoreCase("ASC")){
//            reportProductDTOS = orderItemService.getTop5ProductUnmarketable(month, year, pageable );
//        }
         if (sort.equalsIgnoreCase("DESC")) {
            reportProductDTOS = iOrderDetailService.getTop5ProductBestSell(month, year, pageable );
        }

        if (reportProductDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportProductDTOS, HttpStatus.OK);
    }

    @GetMapping("/count-bill-day/{day}")
    public ResponseEntity<?> countBillOfCurrentDay(@PathVariable String day) {
        return new ResponseEntity<>(iBillService.countBillCurrentDay(day).get(0).getCount(), HttpStatus.OK);
    }

    @GetMapping("/pay-day")
    public ResponseEntity<?> payOfDay( ){
        return new ResponseEntity<>(iBillService.payOfDay(), HttpStatus.OK);
    }





}
