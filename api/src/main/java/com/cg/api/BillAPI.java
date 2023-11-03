package com.cg.api;

import com.cg.domain.dto.bill.*;
import com.cg.domain.entity.Bill;
import com.cg.domain.entity.Order;
import com.cg.exception.DataInputException;
import com.cg.service.bill.BillServiceImpl;
import com.cg.service.bill.IBillService;
import com.cg.service.order.IOrderService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
public class BillAPI {
    @Autowired
    private IBillService iBillService;

    @Autowired
    private BillServiceImpl billService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ValidateUtils validateUtils;


    @GetMapping("/get-bills-today-and-previous-day")
    public ResponseEntity<?> getBillsOfToDayAndPrevious() {
        Date today = new Date();
        Date previousDay = DateUtils.addDays(new Date(),-1);

        List<BillGetTwoDayDTO> listBillToday = iBillService.getBillsNotPaging(today);
        List<BillGetTwoDayDTO> listBillPreviousDay = iBillService.getBillsNotPaging(previousDay);

        if (listBillToday.isEmpty() && listBillPreviousDay.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Map<String, List<?>> result = new HashMap<>();

        result.put("listBillToday", listBillToday);
        result.put("listBillPreviousDay", listBillPreviousDay);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @GetMapping("/{billId}")
    public ResponseEntity<?> getById(@PathVariable Long billId) {

        Optional<Bill> optionalBill = billService.findById(billId);

        if (!optionalBill.isPresent()) {
            throw new DataInputException("Hóa đơn không tồn tại !!!");
        }

        Bill bill = optionalBill.get();
        BillGetAllResDTO billDTO = bill.toBillGetAllResDTO();
        return new ResponseEntity<>(billDTO, HttpStatus.OK);
    }

    @GetMapping("/billDetail/{billId}")
    public ResponseEntity<?> showBillDetail(@PathVariable("billId") String billIdStr) {

        if (!validateUtils.isNumberValid(billIdStr)) {
            throw new DataInputException("Mã lịch sử không hợp lệ");
        }
        Long billId = Long.parseLong(billIdStr);

        billService.findById(billId).orElseThrow(() -> {
            throw new DataInputException("Mã lịch sử không tồn tại");
        });

        List<BillDetailDTO> billDetailDTOS = billService.findBillById(billId);

        return new ResponseEntity<>(billDetailDTOS, HttpStatus.OK);
    }


    @PostMapping("/get-all")
    public ResponseEntity<?> getAllBills(@RequestBody BillFilterReqDTO billFilterReqDTO, @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 20) Pageable pageable) {
        int size = 20;
        int currentPageNumber = billFilterReqDTO.getCurrentPageNumber();

        pageable = PageRequest.of(currentPageNumber, size, Sort.by("id").descending());

        Page<BillGetAllResDTO> billResDTOS = iBillService.findAll(billFilterReqDTO, pageable);
        if (billResDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(billResDTOS, HttpStatus.OK);
    }

    @PostMapping("/day/{day}/{currentPageNumber}")
    public ResponseEntity<?> getBillsOfDay(@PathVariable("day") String day, @PathVariable("currentPageNumber") String currentPageNumberStr, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {

        int size = 20;
        int currentPageNumber = Integer.parseInt(currentPageNumberStr);

        pageable = PageRequest.of(currentPageNumber, size, Sort.by("id").descending());


        Page<BillGetAllResDTO> billGetAllResDTOS = iBillService.getBillsByDay(day, pageable);

        if (billGetAllResDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(billGetAllResDTOS, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createBill(@Validated @RequestParam("tableId") String tableStr ) {

        if(!validateUtils.isNumberValid(tableStr)){
            throw new DataInputException("Mã bàn không hợp lệ");
        }
        Long tableId = Long.parseLong(tableStr);

        Optional<Order> orderOptional = orderService.findByTableId(tableId);

        Order order = orderOptional.get();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn đã thanh toán.");
        }

        BillResDTO billResDTO = iBillService.createBillWithOrders(tableId);

        return new ResponseEntity<>(billResDTO, HttpStatus.OK);
    }

    @PostMapping("/print")
    public ResponseEntity<?> print(@RequestParam("orderId") String orderStr) {
        if (!validateUtils.isNumberValid(orderStr)) {
            throw new DataInputException("ID hóa đơn phải là ký tự số !!!");
        }
        Long orderId = Long.parseLong(orderStr);

        Optional<Order> orderOptional = orderService.findById(orderId);

        Order order = orderOptional.get();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn đã thanh toán !!!");
        }

        List<Bill> billList = iBillService.findALlByOrderIdAndPaid(orderId, false);

        if (billList.size() == 0) {
            throw new DataInputException("Hóa đơn không tồn tại !!!");
        }

        if (billList.size() > 1) {
            throw new DataInputException("Lỗi dữ liệu hệ thống !!!");
        }

        BillPrintTempDTO billPrintItemDTO = iBillService.print(order);

        return new ResponseEntity<>(billPrintItemDTO, HttpStatus.OK);
    }

//    @PostMapping("/pay")
//    public ResponseEntity<?> pay(HttpServletRequest request) throws IOException {
//        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//
//        ObjectMapper mapper = new JsonMapper();
//        JsonNode json = mapper.readTree(body);
//
//        String orderIdStr;
//        String chargePercentStr;
//        String chargeMoneyStr;
//        String discountPercentStr;
//        String discountMoneyStr;
//        String totalAmountStr;
//        String transferPayStr;
//        String cashPayStr;
//
//        try {
//            orderIdStr = json.get("orderId").asText();
//            chargePercentStr = json.get("chargePercent").asText();
//            chargeMoneyStr = json.get("chargeMoney").asText();
//            discountPercentStr = json.get("discountPercent").asText();
//            discountMoneyStr = json.get("discountMoney").asText();
//            totalAmountStr = json.get("totalAmount").asText();
//            transferPayStr = json.get("receivedTransferMoney").asText();
//            cashPayStr = json.get("receivedCashMoney").asText();
//        } catch (Exception e) {
//            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin !!!");
//        }
//
//        if (!validateUtils.isNumberValid(orderIdStr)) {
//            throw new DataInputException("ID hóa đơn phải là ký tự số !!!");
//        }
//        Long orderId = Long.parseLong(orderIdStr);
//
//        if (!validateUtils.isNumberValid(chargePercentStr)) {
//            throw new DataInputException("Phí dịch vụ phải là ký tự số !!!");
//        }
//        Long chargePercent = Long.parseLong(chargePercentStr);
//
//        if (!validateUtils.isNumberValid(chargeMoneyStr)) {
//            throw new DataInputException("Tiền dịch vụ phải là ký tự số !!!");
//        }
//        BigDecimal chargeMoney = BigDecimal.valueOf(Long.parseLong(chargeMoneyStr));
//
//        if (!validateUtils.isNumberValid(discountPercentStr)) {
//            throw new DataInputException("Phí giảm giá phải là ký tự số !!!");
//        }
//        Long discountPercent = Long.parseLong(discountPercentStr);
//
//        if (!validateUtils.isNumberValid(discountMoneyStr)) {
//            throw new DataInputException("Tiền giảm giá phải là ký tự số !!!");
//        }
//        BigDecimal discountMoney = BigDecimal.valueOf(Long.parseLong(discountMoneyStr));
//
//        if (!validateUtils.isNumberValid(totalAmountStr)) {
//            throw new DataInputException("Tổng tiền hóa đơn phải là ký tự số !!!");
//        }
//        BigDecimal totalAmount = BigDecimal.valueOf(Long.parseLong(totalAmountStr));
//
//        if (!validateUtils.isNumberValid(transferPayStr)) {
//            throw new DataInputException("Số tiền chyển khoản phải là ký tự số !!!");
//        }
//        BigDecimal transferPay = BigDecimal.valueOf(Long.parseLong(transferPayStr));
//
//        if (!validateUtils.isNumberValid(cashPayStr)) {
//            throw new DataInputException("Số tiền khách trả phải là ký tự số !!!");
//        }
//        BigDecimal cashPay = BigDecimal.valueOf(Long.parseLong(cashPayStr));
//
//
//        Order order = orderService.findById(orderId).orElseThrow(() -> {
//            throw new DataInputException("Hóa đơn không tồn tại !!!");
//        });
//
//        if (order.getPaid() == true) {
//            throw new DataInputException("Hóa đơn đã thanh toán !!!");
//        }
//
//
//        Optional<Bill> billOptional = iBillService.findByOrderAndPaid(order, true);
//
//        if (billOptional.isPresent()) {
//            throw new DataInputException("Hóa đơn đã được thanh toán !!!");
//        }
//
//        iBillService.pay(orderId, chargePercent, chargeMoney, discountPercent, discountMoney, totalAmount, transferPay, cashPay);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//
//    }







}
