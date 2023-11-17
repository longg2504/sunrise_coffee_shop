package com.cg.service.bill;

import com.cg.domain.dto.bill.*;
import com.cg.domain.dto.report.*;
import com.cg.domain.entity.Bill;
import com.cg.domain.entity.Order;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IBillService extends IGeneralService<Bill, Long> {

    BillPrintTempDTO print(Order order);

    void pay(Long orderId, Long chargePercent, BigDecimal chargeMoney, Long discountPercent, BigDecimal discountMoney, BigDecimal totalAmount, BigDecimal transferPay, BigDecimal cashPay);

    Optional<Bill> findBillByOrderId(Long orderId);

    Optional<Bill> findByOrderAndPaid(Order order, Boolean paid);

    List<Bill> findALlByOrderIdAndPaid(Long orderId, Boolean paid);

    List<YearReportDTO> getReportByYear(int year);

    List<IYearReportDTO> getReportByCurrentYear();

    List<I6MonthAgoReportDTO> getReport6MonthAgo();

    YearReportDTO getReportByMonth(int month, int year);


    IReportDTO getReportOfCurrentDay();

    IReportDTO getReportOfCurrentMonth();

    ReportDTO getReportOfDay(String day);

    List<IDayToDayReportDTO> getReportFrom10DaysAgo();

    List<DayToDayReportDTO> getReportFromDayToDay(String startDay, String endDay);

    List<BillOfTheDayDTO> countBillCurrentDay(String day);

    Page<BillGetAllResDTO> findAll(BillFilterReqDTO billFilterReqDTO, Pageable pageable);

    BillResDTO createBillWithOrders(Long tableId);

    PayDTO payOfDay();

    Page<BillGetAllResDTO> getBillsByDay(String day, Pageable pageable);

    List<BillGetTwoDayDTO> getBillsNotPaging(String day);

    List<BillGetTwoDayDTO> getBillsNotPaging(Date day);

    List<BillDetailDTO> findBillById(Long billId);

    Page<BillGetAllResDTO> getBillByDate(Integer year, Integer month, Integer day,String staffName, Pageable pageable);

    Page<BillGetAllResDTO> getBillByStaff(String staffName, Pageable pageable);


}
