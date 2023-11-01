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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.crypto.MacSpi;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

        if (orderDetailList.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderDetailList, HttpStatus.OK);
    }

    //get-all-order-detail-status-cooking-group-by-table-for-kitchen
    @GetMapping("/kitchen/get-by-status-cooking-group-by-table")
    public ResponseEntity<?> getBystatusCookingGroupByTable() {
        List<OrderKitchenTableDTO> orderList = orderService.getAllOrderKitchenCookingByTable(EOrderDetailStatus.COOKING);

        if (orderList.size() == 0) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orderList, HttpStatus.OK);


    }

    //change-status-one-product-of-table-from-cooking-to-waiting
    @PostMapping("/kitchen/table/change-status-cooking-to-waiting")
    public ResponseEntity<?> changeStatusFromCookingToWaiterOfProduct(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hoá đơn phải là ký tự !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hoá đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.COOKING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hoá đơn '%s' không có sản phẩm tương ứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();

        if (order.getPaid()) {
            throw new DataInputException("Hoá đơn này đã được thanh toán !!!");
        }

        OrderDetailKitchenWaiterDTO orderDetailKitchenWaiterDTO = orderDetailService.changeStatusFromCookingToWaiterOfProduct(orderDetail);

        return new ResponseEntity<>(orderDetailKitchenWaiterDTO, HttpStatus.OK);
    }

    //change-status-all-of-one-product-of-table-from-cooking-to-waiting
    @PostMapping("/kitchen/table/change-all-status-cooking-to-waiting")
    public ResponseEntity<?> changeStatusAllFromCookingToWaiterOfProduct(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.COOKING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương ứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán");
        }

        orderDetailService.changeStatusFromCookingToWaitingAllToProductOfOrder(orderDetail);

        Map<String, List<?>> result = getAllItem();

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    //change-status-all-product-in-table-from-cooking-to-waiting
    @PostMapping("/kitchen/table/change-status-cooking-to-waiting-all-products")
    public ResponseEntity<?> changeStatusFromCookingToWaitingAllProductOfTable(@RequestParam("orderId") String orderIdStr) {
        if (!validateUtils.isNumberValid(orderIdStr)) {
            throw new DataInputException("ID hóa đơn phải là ký tự số !!!");
        }

        Long orderId = Long.parseLong(orderIdStr);

        Order order = orderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("Hóa đơn không tồn tại");
        });

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán");
        }

        orderDetailService.changeStatusFromCookingToWaitingAllToProductOfOrderOfTable(order);

        Map<String, List<?>> result = getAllItem();

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    //change-status-one-product-from-cooking-to-waiting-of-group-product
    @PostMapping("/kitchen/product/change-status-cooking-to-waiting-one-product")
    public ResponseEntity<?> changeStatusOneProductFromCookingToWaitingOfGroupProduct(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String productIdStr;
        String note;

        try {
            productIdStr = json.get("productId").asText();
            note = Objects.equals(json.get("note").asText(), "null") ? null : json.get("note").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("ID sản phẩm phải là ký tự số");
        }

        Long productId = Long.parseLong(productIdStr);

        OrderDetailKitchenWaiterDTO orderDetailKitchenWaiterDTO = orderDetailService.changeStatusFromCookingToWaiterToOneProductOfGroup(productId, note);

        return new ResponseEntity<>(orderDetailKitchenWaiterDTO, HttpStatus.OK);
    }

    //change-status-all-product-from-cooking-to-waiting-of-group-product
    @PostMapping("/kitchen/product/change-status-cooking-to-waiting-all-product")
    public ResponseEntity<?> changeStatusAllProductFromCookingToWaitingOfGroupProduct(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String productIdStr;
        String note;

        try {
            productIdStr = json.get("productId").asText();
            note = Objects.equals(json.get("note").asText(), "null") ? null : json.get("note").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("ID sản phẩm phải là ký tự số");
        }

        Long productId = Long.parseLong(productIdStr);

        orderDetailService.changeStatusFromCookingToWaiterToAllProductsOfGroup(productId, note);

        Map<String, List<?>> result = getAllItem();

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    //change-status-one-product-of-table-from-waiting-to-delivery
    @PostMapping("/kitchen/product/change-status-waiting-to-delivery-one-product-of-table")
    public ResponseEntity<?> changeStatusFromWaitingToDeliveryOfProduct(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }
        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.WAITING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();
        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromWaiterToDeliveryOfProduct(orderDetail);

        List<IOrderDetailKitchenWaiterDTO> itemsWaiter = orderDetailService.getOrderDetailByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    //change-status-all-product-of-table-from-waiting-to-delivery
    @PostMapping("/kitchen/table/change-status-waiting-to-delivery-all-product-of-table")
    public ResponseEntity<?> changeStatusFromWaitingToDeliveryAllProductOfOrder(@RequestParam("orderDetailId") String orderDetailStr) {

        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.WAITING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();
        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromWaiterToDeliveryToProductOfOrder(orderDetail);

        List<IOrderDetailKitchenWaiterDTO> itemsWaiter = orderDetailService.getOrderDetailByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    //chang-all-product-status-in-table-from-waiter-to-delivery
    @PostMapping("/kitchen/table/change-status-waiting-to-delivery-all-products")
    public ResponseEntity<?> changeStatusFromWaitingToDeliveryToTableAll(@RequestParam("orderId") String orderStr) {
        if (!validateUtils.isNumberValid(orderStr)) {
            throw new DataInputException("ID hóa đơn phải là ký tự số");
        }

        Long orderId = Long.parseLong(orderStr);

        Order order = orderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("Hóa đơn không hợp lệ");
        });

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán");
        }

        orderDetailService.changeStatusFromWaiterToDeliveryAllProductOfTable(order);

        List<OrderDetailResDTO> orderDetailResDTOS = orderDetailService.getOrderDetailResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderDetailResDTOS, HttpStatus.OK);
    }


    //change-status-one-product-of-table-from-waiting-to-done
    @PostMapping("/kitchen/product/change-status-waiting-to-done-one-product-of-table")
    public ResponseEntity<?> changeStatusFromWaitingToDoneOfProduct(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }
        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.WAITING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();
        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromWaiterToDoneOfProduct(orderDetail);

        List<IOrderDetailKitchenWaiterDTO> itemsWaiter = orderDetailService.getOrderDetailByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    //change-status-all-product-of-table-from-waiting-to-done
    @PostMapping("/kitchen/table/change-status-waiting-to-done-all-product-of-table")
    public ResponseEntity<?> changeStatusFromWaitingToDoneAllProductOfOrder(@RequestParam("orderDetailId") String orderDetailStr) {

        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.WAITING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương tứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();
        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromWaiterToDoneToProductOfOrder(orderDetail);

        List<IOrderDetailKitchenWaiterDTO> itemsWaiter = orderDetailService.getOrderDetailByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }

    //change-all-product-status-in-table-from-waiter-to-done
    @PostMapping("/kitchen/table/change-status-waiting-to-done-all-products")
    public ResponseEntity<?> changeStatusFromWaitingToDoneToTableAll(@RequestParam("orderId") String orderStr) {
        if (!validateUtils.isNumberValid(orderStr)) {
            throw new DataInputException("ID hóa đơn phải là ký tự số");
        }

        Long orderId = Long.parseLong(orderStr);

        Order order = orderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("Hóa đơn không hợp lệ");
        });

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán");
        }

        orderDetailService.changeStatusFromWaiterToDoneAllProductOfTable(order);

        List<OrderDetailResDTO> orderDetailResDTOS = orderDetailService.getOrderDetailResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderDetailResDTOS, HttpStatus.OK);
    }

    //change-status-one-product-of-table-from-cooking-to-stock-out
    @PostMapping("/kitchen/table/change-status-cooking-to-stock-out")
    public ResponseEntity<?> changeStatusFromCookingToStockOutOfProduct(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.COOKING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương ứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();
        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán");
        }

        OrderDetailKitchenWaiterDTO orderDetailKitchenWaiterDTO = orderDetailService.changeStatusFromCookingToStockOutOfProduct(orderDetail);

        return new ResponseEntity<>(orderDetailKitchenWaiterDTO, HttpStatus.OK);
    }

    //change-all-status-of-products-in-table-from-cooking-to-stock-out
    @PostMapping("/kitchen/table/change-status-cooking-to-stock-out-to-product")
    public ResponseEntity<?> changeStatusFromCookingToStockOutToProductOfTable(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.COOKING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương ứng trạng thái", tableName));
        }

        Order order = orderDetail.getOrder();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán");
        }

        orderDetailService.changeStatusFromCookingToStockOutToProduct(orderDetail);

        List<IOrderDetailKitchenGroupDTO> itemsCooking = orderDetailService.getOrderDetailByStatusCookingGroupByProduct();

        List<OrderKitchenTableDTO> itemsTable = orderService.getAllOrderKitchenCookingByTable(EOrderDetailStatus.COOKING);

        Map<String, List<?>> result = new HashMap<>();

        result.put("itemsCooking", itemsCooking);
        result.put("itemsTable", itemsTable);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    //change-status-one-product-of-table-from-waiting-to-stock-out
    @PostMapping("/kitchen/product/change-status-waiting-to-stock-out")
    public ResponseEntity<?> changeStatusFromWaitingToStockOutOfProduct(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết Hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.WAITING)) {
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương ứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromWaiterToStockOutOfProduct(orderDetail);

        List<IOrderDetailKitchenWaiterDTO> itemsWaiting = orderDetailService.getOrderDetailByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiting, HttpStatus.OK);
    }


    //change-all-status-of-product-in-table-from-waiter-to-stock-out
    @PostMapping("/kitchen/table/change-status-waiting-to-stock-out-to-product")
        public ResponseEntity<?> changeStatusFromWaitingToStockOutToProductOfOrder(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.WAITING)){
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương ứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromWaiterToStockOutToProductOfOrder(orderDetail);
        List<IOrderDetailKitchenWaiterDTO> itemsWaiter = orderDetailService.getOrderDetailByStatusWaiterGroupByTableAndProduct();

        return new ResponseEntity<>(itemsWaiter, HttpStatus.OK);
    }


    //change-status-one-product-of-table-from-delivery-to-done
    @PostMapping("/kitchen/product/change-status-delivery-to-done")
    public ResponseEntity<?> changeStatusFromDeliveryToDoneOfProduct(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.DELIVERY)){
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương ứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromDeliveryToDoneOfProduct(orderDetail);

        List<OrderDetailResDTO> orderDetailResDTOS = orderDetailService.getOrderDetailResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderDetailResDTOS, HttpStatus.OK);
    }

    //change-all-status-of-product-in-table-from-delivery-to-done
    @PostMapping("/kitchen/table/change-status-delivery-to-done-to-product")
    public ResponseEntity<?> changeStatusFromDeliveryToDoneToProductOfOrder(@RequestParam("orderDetailId") String orderDetailStr) {
        if (!validateUtils.isNumberValid(orderDetailStr)) {
            throw new DataInputException("ID chi tiết hóa đơn phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("ID chi tiết hóa đơn không tồn tại !!!");
        });

        if (!orderDetail.getStatus().equals(EOrderDetailStatus.DELIVERY)){
            String tableName = orderDetail.getOrder().getTableOrder().getTitle();
            throw new DataInputException(String.format("Hóa đơn '%s' không có sản phẩm tương ứng trạng thái !!!", tableName));
        }

        Order order = orderDetail.getOrder();

        if (order.getPaid()) {
            throw new DataInputException("Hóa đơn này đã thanh toán !!!");
        }

        orderDetailService.changeStatusFromDeliveryToDoneToProductOfOrder(orderDetail);

        List<OrderDetailResDTO> orderDetailResDTOS = orderDetailService.getOrderDetailResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderDetailResDTOS, HttpStatus.OK);
    }

    //change-all-status-product-in-table-from-delivery-to-done
    @PostMapping("kitchen/table/change-status-delivery-to-done-all-products")
    public ResponseEntity<?> changeStatusFromDeliveryToDoneToTableAll(@RequestParam("orderId") String orderStr) {
        if(!validateUtils.isNumberValid(orderStr)){
            throw new DataInputException("ID hóa đơn phải là ký tự số !!!");
        }

        Long orderId = Long.parseLong(orderStr);

        Order order = orderService.findById(orderId).orElseThrow(() -> {
            throw new DataInputException("Hóa đơn không hợp lệ");
        });

        if(order.getPaid()){
            throw new DataInputException("Hóa đơn này đã thanh toán");
        }

        orderDetailService.changeStatusFromDeliveryToDoneToTableAll(order);
        List<OrderDetailResDTO> orderDetailResDTOS = orderDetailService.getOrderDetailResDTOByOrderId(order.getId());

        return new ResponseEntity<>(orderDetailResDTOS, HttpStatus.OK);
    }


    @DeleteMapping("/delete-item-stock-out")
    public ResponseEntity<?> deleteItemStock(@RequestParam("orderDetailId") String orderDetailStr) {
        if(!validateUtils.isNumberValid(orderDetailStr)){
            throw new DataInputException("ID sản phẩm phải là ký tự số !!!");
        }

        Long orderDetailId = Long.parseLong(orderDetailStr);

        OrderDetail orderDetail = orderDetailService.findById(orderDetailId).orElseThrow(() -> {
            throw new DataInputException("Sản phẩm không tồn tại trong hóa đơn !!!");
        });

        if(!orderDetail.getStatus().equals(EOrderDetailStatus.STOCK_OUT)){
            throw new DataInputException("Sản phẩm không hợp lệ !!!");
        }

        orderDetailService.deleteOrderDetailStockOut(orderDetail);

        Map<String, Long> result = new HashMap<>();
        result.put("orderDetailId", orderDetailId);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}


