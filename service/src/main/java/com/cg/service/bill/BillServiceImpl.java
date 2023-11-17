package com.cg.service.bill;

import com.cg.domain.dto.bill.*;
import com.cg.domain.dto.orderDetail.OrderDetailDTO;
import com.cg.domain.dto.report.*;
import com.cg.domain.entity.*;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.exception.UnauthorizedException;
import com.cg.repository.bill.BillRepository;
import com.cg.repository.billBackUp.BillBackupRepository;
import com.cg.repository.order.OrderRepository;
import com.cg.repository.orderDetail.OrderDetailRepository;
import com.cg.repository.tableOrder.TableOrderRepository;
import com.cg.repository.tableOrderBackup.TableOrderBackupRepository;
import com.cg.service.billBackup.IBillBackupService;
import com.cg.service.order.IOrderService;
import com.cg.service.orderDetail.IOrderDetailService;
import com.cg.service.staff.IStaffService;
import com.cg.service.tableOrder.ITableOrderService;
import com.cg.service.tableOrderBackup.ITableOrderBackupService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillServiceImpl implements IBillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    IBillService billService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Autowired
    private TableOrderRepository tableOrderRepository;

    @Autowired
    private ITableOrderService tableOrderService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ITableOrderBackupService tableOrderBackupService;

    @Autowired
    private TableOrderBackupRepository tableOrderBackupRepository;

    @Autowired
    private IBillBackupService billBackupService;

    @Autowired
    private BillBackupRepository billBackupRepository;


    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return billRepository.findById(id);
    }

    @Override
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    @Override
    public void delete(Bill bill) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public BillPrintTempDTO print(Order order) {
        Bill bill = billService.findBillByOrderId(order.getId()).orElseThrow(() -> {
            throw new DataInputException("ID hóa đơn không tồn tại.");
        });

        List<BillPrintItemDTO> items = orderDetailRepository.getAllBillPrintItemDTOByOrderId(order.getId());

        return bill.toBillPrintTempDTO(items, order.getCreatedAt());

    }

    @Override
    public void pay(Long orderId, Long chargePercent, BigDecimal chargeMoney, Long discountPercent, BigDecimal discountMoney, BigDecimal totalAmount, BigDecimal transferPay, BigDecimal cashPay) {
        Optional<TableOrderBackup> tableBackupOptional = tableOrderBackupService.findByOrderCurrentId(orderId);

        if(tableBackupOptional.isPresent()) {

            List<BillBackup> currentTableBillBackup = billBackupService.findAllByOrderId(tableBackupOptional.get().getOrderCurrentId());

            if (currentTableBillBackup.size() == 0) {
                throw new DataInputException("đơn hàng hiện tại không hợp lệ, vui lòng kiểm tra lại dữ liệu !!!");
            }

            List<BillBackup> targetTableBillBackup = billBackupService.findAllByOrderId(tableBackupOptional.get().getOrderTargetId());

            if (targetTableBillBackup.size() == 0) {
                throw new DataInputException("đơn hàng muốn tách không hợp lệ, vui lòng kiểm tra lại dữ liệu !!!");
            }

            billBackupRepository.deleteAll(currentTableBillBackup);
            billBackupRepository.deleteAll(targetTableBillBackup);
            tableOrderBackupRepository.delete(tableBackupOptional.get());
        }



        Staff staff = staffService.findByUsername(appUtils.getPrincipalUsername()).orElseThrow(() -> {
            throw new UnauthorizedException("Vui lòng xác thực");
        });

        Order order = orderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("ID order không tồn tại.");
        });

        order.setPaid(true);
        order = orderRepository.save(order);

        List<OrderDetail> orderItems = orderDetailService.getAllByOrder(order);
        orderItems.forEach(item -> item.setStatus(EOrderDetailStatus.DONE));



        Optional<Bill> billOptional = billRepository.findBillByOrderId(order.getId());

        Bill bill = billOptional.get()
                .setOrder(order)
                .setStaff(staff)
                .setTable(order.getTableOrder())
                .setOrderPrice(order.getTotalAmount())
                .setChargePercent(chargePercent)
                .setChargeMoney(chargeMoney)
                .setDiscountPercent(discountPercent)
                .setDiscountMoney(discountMoney)
                .setTotalAmount(totalAmount)
                .setTransferPay(transferPay)
                .setCashPay(cashPay)
                .setPaid(true);
        billRepository.save(bill);


        TableOrder table = order.getTableOrder();
        table.setStatus(ETableStatus.EMPTY);
        tableOrderRepository.save(table);
    }

    // don't use this api !!!
    @Override
    public BillResDTO createBillWithOrders(Long tableId) {
        Staff staff = staffService.findByUsername(appUtils.getPrincipalUsername()).orElseThrow(() -> {
            throw new DataInputException("Tên nhân viên không hợp lệ");
        });

        Order order = orderService.findByTableId(tableId).orElseThrow(() -> new DataInputException("ID Hóa đơn không hợp lệ."));
        order.setPaid(true);
        order = orderRepository.save(order);

        List<OrderDetail> orderItems = orderDetailService.getAllByOrder(order);
        orderItems.forEach(item -> item.setStatus(EOrderDetailStatus.DONE));
        orderDetailRepository.saveAll(orderItems);

        TableOrder table = order.getTableOrder();
        table.setStatus(ETableStatus.EMPTY);
        tableOrderRepository.save(table);

            Bill bill = new Bill()
                    .setOrder(order)
                    .setStaff(staff)
                    .setTable(order.getTableOrder())
                    .setDiscountPercent(0L)
                    .setDiscountMoney(BigDecimal.ZERO)
                    .setChargePercent(0L)
                    .setChargeMoney(order.getTotalAmount())
                    .setOrderPrice(order.getTotalAmount())
                    .setTotalAmount(order.getTotalAmount())
                    .setPaid(true)
                    .setCashPay(BigDecimal.ZERO)
                    .setTransferPay(BigDecimal.ZERO)
                    ;
            billRepository.save(bill);
            return bill.toBillResDTO();}

    @Override
    public Optional<Bill> findBillByOrderId(Long orderId) {
        return billRepository.findBillByOrderId(orderId);
    }

    @Override
    public Optional<Bill> findByOrderAndPaid(Order order, Boolean paid) {
        return billRepository.findBillByOrderAndPaid(order, paid);
    }

    @Override
    public List<Bill> findALlByOrderIdAndPaid(Long orderId, Boolean paid) {
        return billRepository.findAllByOrderIdAndPaid(orderId, paid);
    }

    @Override
    public List<YearReportDTO> getReportByYear(int year) {
        return billRepository.getReportByYear(year);
    }

    @Override
    public List<IYearReportDTO> getReportByCurrentYear() {
        return billRepository.getReportByCurrentYear();
    }

    @Override
    public List<I6MonthAgoReportDTO> getReport6MonthAgo() {
        return billRepository.getReport6MonthAgo();
    }

    @Override
    public YearReportDTO getReportByMonth(int month, int year) {
        return billRepository.getReportByMonth(month, year);
    }

    @Override
    public IReportDTO getReportOfCurrentDay() {
        return billRepository.getReportOfCurrentDay();
    }

    @Override
    public IReportDTO getReportOfCurrentMonth() {
        return billRepository.getReportOfCurrentMonth();
    }

    @Override
    public ReportDTO getReportOfDay(String day) {
        return billRepository.getReportOfDay(day);
    }

    @Override
    public List<IDayToDayReportDTO> getReportFrom10DaysAgo() {
        return billRepository.getReportFrom10DaysAgo();
    }

    @Override
    public List<DayToDayReportDTO> getReportFromDayToDay(String startDay, String endDay) {
        return billRepository.getReportFromDayToDay(startDay, endDay);
    }

    @Override
    public List<BillOfTheDayDTO> countBillCurrentDay(String day) {
        return billRepository.countBillCurrentDay(day);
    }

    @Override
    public Page<BillGetAllResDTO> findAll(BillFilterReqDTO billFilterReqDTO, Pageable pageable) {
        return billRepository.findAll(billFilterReqDTO, pageable).map(Bill::toBillGetAllResDTO);
    }



    @Override
    public PayDTO payOfDay() {
        return billRepository.payOfDay();
    }

    @Override
    public Page<BillGetAllResDTO> getBillsByDay(String day, Pageable pageable) {
        return billRepository.getBillsByDay(day, pageable);
    }

    @Override
    public List<BillGetTwoDayDTO> getBillsNotPaging(String day) {
        return billRepository.getBillsNotPaging(day);
    }

    @Override
    public List<BillGetTwoDayDTO> getBillsNotPaging(Date day) {
        return billRepository.getBillsNotPaging(day);
    }

    @Override
    public List<BillDetailDTO> findBillById(Long billId) {
        return billRepository.findBillById(billId);
    }

    @Override
    public Page<BillGetAllResDTO> getBillByDate(Integer year, Integer month, Integer day,Pageable pageable) {
        LocalDate start = getDate(year,month,day);
        if(day == null){
            return billRepository.getAllBillByDate(start,getLastDayOfMonth(start),pageable);
        }

        return billRepository.getAllBillByDate(start, start,pageable);
    }

    @Override
    public Page<BillGetAllResDTO> getBillByStaff(String staffName, Pageable pageable) {
        return billRepository.getBillByStaff(staffName, pageable);
    }

    public LocalDate getDate(int year, int month, Integer day) {
        if (day == null) {
            // Trả về ngày đầu tháng
            return LocalDate.of(year, month, 1);
        } else {
            // Trả về ngày tháng của năm
            return LocalDate.of(year, month, day);
        }
    }

    public LocalDate getLastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }



}
