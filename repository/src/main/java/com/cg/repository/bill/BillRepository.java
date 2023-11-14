package com.cg.repository.bill;

import com.cg.domain.dto.bill.*;
import com.cg.domain.dto.report.*;
import com.cg.domain.entity.Bill;
import com.cg.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {

    @Query("SELECT NEW com.cg.domain.dto.bill.BillDetailDTO (" +
            "b.id," +
            "b.totalAmount," +
            "od.note," +
            "od.price," +
            "od.quantity," +
            "p.title," +
            "od.createdAt" +
            ") " +
            "FROM Bill AS b " +
            "JOIN Order AS o ON b.order.id = o.id " +
            "JOIN OrderDetail AS od ON od.order.id = o.id " +
            "JOIN Product AS p ON od.product.id = p.id " +
            "WHERE b.id =  :billId"
    )
    List<BillDetailDTO> findBillById(@Param("billId") Long billId);

    @Query("SELECT NEW com.cg.domain.entity.Bill (" +
            "bi.id," +
            "bi.orderPrice," +
            "bi.discountMoney," +
            "bi.discountPercent," +
            "bi.chargeMoney," +
            "bi.chargePercent," +
            "bi.totalAmount," +
            "bi.table," +
            "bi.staff," +
            "bi.order," +
            "bi.paid," +
            "bi.cashPay," +
            "bi.transferPay " +
            ") " +
            "FROM Bill AS bi " +
            "WHERE bi.deleted = false " +
            "AND bi.order.id = :orderId "

    )
    Optional<Bill> findBillByOrderId(@Param("orderId") Long orderId);

    Optional<Bill> findBillByOrderAndPaid(Order order, Boolean paid);

    List<Bill> findAllByOrderIdAndPaid(Long orderId, Boolean paid);


    @Query("SELECT NEW com.cg.domain.dto.report.YearReportDTO (" +
            "MONTH(b.createdAt), " +
            "SUM(b.totalAmount), " +
            "count(b.id) " +
            ") " +
            "FROM Bill AS b " +
            "WHERE MONTH(b.createdAt) = :month " +
            "AND YEAR(b.createdAt) = :year " +
            "AND b.paid = true " +
            "GROUP BY MONTH(b.createdAt)"
    )

    YearReportDTO getReportByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT NEW com.cg.domain.dto.report.YearReportDTO (" +
            "MONTH(b.createdAt)," +
            "SUM(b.totalAmount)," +
            "COUNT(b.id)" +
            ") " +
            "FROM Bill AS b " +
            "WHERE YEAR(b.createdAt) = :year " +
            "AND b.paid = true " +
            "GROUP BY MONTH(b.createdAt) " +
            "ORDER BY MONTH(b.createdAt) ASC"
    )
    List<YearReportDTO> getReportByYear(@Param("year") int year);

    @Query(value = "SELECT * FROM v_get_report_current_year", nativeQuery = true)
    List<IYearReportDTO> getReportByCurrentYear();

    @Query(value = "SELECT * FROM v_get_report_of_current_day", nativeQuery = true)
    IReportDTO getReportOfCurrentDay();
    @Query(value = "SELECT * FROM v_get_report_of_current_month", nativeQuery = true)
    IReportDTO getReportOfCurrentMonth();

    @Query(value = "SELECT * FROM v_get_report_6_month_ago", nativeQuery = true)
    List<I6MonthAgoReportDTO> getReport6MonthAgo();

    @Query(value = "SELECT * FROM v_get_report_from_10_days_ago", nativeQuery = true)
    List<IDayToDayReportDTO> getReportFrom10DaysAgo();

    @Query("SELECT NEW com.cg.domain.dto.report.ReportDTO (" +
            "SUM(b.totalAmount)," +
            "COUNT(b.id) " +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE_FORMAT(b.createdAt, '%Y-%m-%d') = :day " +
            "AND b.paid = true "
    )
    ReportDTO getReportOfDay(@Param("day") String day);

    @Query("SELECT NEW com.cg.domain.dto.report.DayToDayReportDTO (" +
            "DATE_FORMAT(b.createdAt,'%d/%m/%Y'), " +
            "SUM(b.totalAmount) " +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE_FORMAT(b.createdAt,'%Y-%m-%d') > :startDay " +
            "AND DATE_FORMAT(b.createdAt,'%Y-%m-%d') < :endDay " +
            "AND b.paid = true " +
            "GROUP BY DATE_FORMAT(b.createdAt,'%d/%m/%Y') " +
            "ORDER BY DATE_FORMAT(b.createdAt,'%d/%m/%Y') "
    )
    List<DayToDayReportDTO> getReportFromDayToDay(@Param("startDay") String startDay, @Param("endDay") String endDay);


    @Query("SELECT NEW com.cg.domain.dto.report.BillOfTheDayDTO (" +
            "COUNT(b.id) " +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE_FORMAT(b.createdAt,'%Y-%m-%d') = :day " +
            "AND b.paid = true "
    )
    List<BillOfTheDayDTO> countBillCurrentDay(@Param("day") String day);


    @Query("SELECT NEW com.cg.domain.dto.report.PayDTO (" +
            "SUM(b.cashPay), " +
            "SUM(b.transferPay)" +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE_FORMAT(b.createdAt,'%Y-%m-%d') = CURRENT_DATE " +
            "AND b.paid = true "
    )
    PayDTO payOfDay();


    @Query("SELECT NEW com.cg.domain.dto.bill.BillGetAllResDTO (" +
            "b.id, " +
            "b.createdAt, " +
            "b.orderPrice, " +
            "b.discountMoney, " +
            "b.discountPercent, " +
            "b.chargeMoney, " +
            "b.chargePercent, " +
            "b.totalAmount, " +
            "b.table.id, " +
            "b.table.title, " +
            "b.order.id, " +
            "b.staff.id, " +
            "b.staff.fullName, " +
            "b.paid"  +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE_FORMAT(b.createdAt,'%Y-%m-%d') = :day " +
            "AND b.paid = true "
    )
    Page<BillGetAllResDTO> getBillsByDay(@Param("day") String day, Pageable pageable);

    @Query("SELECT NEW com.cg.domain.dto.bill.BillGetTwoDayDTO (" +
            "b.id, " +
            "b.createdAt, " +
            "b.orderPrice, " +
            "b.discountMoney, " +
            "b.discountPercent, " +
            "b.chargeMoney, " +
            "b.chargePercent, " +
            "b.totalAmount, " +
            "b.table.id, " +
            "b.table.title, " +
            "b.order.id, " +
            "b.staff.id, " +
            "b.staff.fullName, " +
            "b.paid"  +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE_FORMAT(b.createdAt, '%Y-%m-%d') = :day " +
//            "WHERE DATE(bill.createdAt) = :day " +
            "AND b.paid = true "
    )
    List<BillGetTwoDayDTO> getBillsNotPaging(@Param("day") String day);

    @Query("SELECT NEW com.cg.domain.dto.bill.BillGetTwoDayDTO (" +
            "b.id, " +
            "b.createdAt, " +
            "b.orderPrice, " +
            "b.discountMoney, " +
            "b.discountPercent, " +
            "b.chargeMoney, " +
            "b.chargePercent, " +
            "b.totalAmount, " +
            "b.table.id, " +
            "b.table.title, " +
            "b.order.id, " +
            "b.staff.id, " +
            "b.staff.fullName, " +
            "b.paid"  +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE(b.createdAt) = :day " +
            "AND b.paid = true "
    )
    List<BillGetTwoDayDTO> getBillsNotPaging(@Param("day") Date day);


    default Page<Bill> findAll(BillFilterReqDTO billFilterReqDTO, Pageable pageable) {
        return findAll((root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            Date billFrom = billFilterReqDTO.getBillFrom();
            Date billTo = billFilterReqDTO.getBillTo();

            if (billFrom != null && billTo != null) {
                Predicate predicate = criteriaBuilder.between(root.get("createdAt") ,billFrom, billTo);
                predicates.add(predicate);
            }
            else if (billFrom != null || billTo != null) {
                if (billFrom != null) {
                    Predicate predicate = criteriaBuilder.greaterThan(root.get("createdAt"), billFrom);
                    predicates.add(predicate);
                }

                if (billTo != null) {
                    Predicate predicate = criteriaBuilder.lessThan(root.get("createdAt"), billTo);
                    predicates.add(predicate);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageable);

    }



    @Query("SELECT NEW com.cg.domain.dto.bill.BillGetAllResDTO (" +
            "b.id, " +
            "b.createdAt, " +
            "b.orderPrice, " +
            "b.discountMoney, " +
            "b.discountPercent, " +
            "b.chargeMoney, " +
            "b.chargePercent, " +
            "b.totalAmount, " +
            "b.table.id, " +
            "b.table.title, " +
            "b.order.id, " +
            "b.staff.id, " +
            "b.staff.fullName, " +
            "b.paid"  +
            ") " +
            "FROM Bill AS b " +
            "WHERE DATE_FORMAT(b.createdAt, '%Y-%m-%d') >= DATE_FORMAT(:start, '%Y-%m-%d') " +
            "AND DATE_FORMAT(b.createdAt, '%Y-%m-%d') <= DATE_FORMAT(:end, '%Y-%m-%d')"
    )
    Page<BillGetAllResDTO> getAllBillByDate(LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT NEW com.cg.domain.dto.bill.BillGetAllResDTO (" +
            "b.id, " +
            "b.createdAt, " +
            "b.orderPrice, " +
            "b.discountMoney, " +
            "b.discountPercent, " +
            "b.chargeMoney, " +
            "b.chargePercent, " +
            "b.totalAmount, " +
            "b.table.id, " +
            "b.table.title, " +
            "b.order.id, " +
            "b.staff.id, " +
            "b.staff.fullName, " +
            "b.paid"  +
            ") " +
            "FROM Bill AS b " +
            "WHERE b.staff.fullName like :staffName " +
            "AND b.paid = true "
    )
    Page<BillGetAllResDTO> getBillByStaff(@Param("staffName") String staffName, Pageable pageable);




}
