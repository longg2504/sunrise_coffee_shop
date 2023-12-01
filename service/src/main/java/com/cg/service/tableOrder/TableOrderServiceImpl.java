package com.cg.service.tableOrder;

import com.cg.domain.dto.product.ProductSplitReqDTO;
import com.cg.domain.dto.tableOrder.*;
import com.cg.domain.entity.*;
import com.cg.domain.enums.EOrderDetailStatus;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.repository.bill.BillRepository;
import com.cg.repository.billBackUp.BillBackupRepository;
import com.cg.repository.order.OrderRepository;
import com.cg.repository.orderDetail.OrderDetailRepository;
import com.cg.repository.product.ProductRepository;
import com.cg.repository.staff.StaffRepository;
import com.cg.repository.tableOrder.TableOrderRepository;
import com.cg.repository.tableOrderBackup.TableOrderBackupRepository;
import com.cg.repository.user.UserRepository;
import com.cg.repository.zone.ZoneRepository;
import com.cg.service.billBackup.IBillBackupService;
import com.cg.service.order.IOrderService;
import com.cg.service.orderDetail.IOrderDetailService;
import com.cg.service.tableOrderBackup.ITableOrderBackupService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class TableOrderServiceImpl implements ITableOrderService {

    @Autowired
    private TableOrderRepository tableOrderRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private IOrderDetailService iOrderDetailService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private BillBackupRepository billBackupRepository;

    @Autowired
    private IBillBackupService ibillBackupService;

    @Autowired
    private TableOrderBackupRepository tableOrderBackupRepository;

    @Autowired
    private ITableOrderBackupService iTableOrderBackupService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<TableOrder> findAll() {
        return tableOrderRepository.findAll();
    }

    @Override
    public Optional<TableOrder> findById(Long id) {
        return tableOrderRepository.findById(id);
    }

    @Override
    public TableOrder save(TableOrder tableOrder) {
        return tableOrderRepository.save(tableOrder);
    }

    @Override
    public void delete(TableOrder tableOrder) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Page<TableOrderDTO> findAllTableOrder(Zone zone, String status, String search, Pageable pageable) {
        return tableOrderRepository.findAllTableOrderByTitle(zone, status, search, pageable);
    }

    @Override
    public List<TableOrderDTO> findAllTablesWithoutSenderId(Long tableId) {
        return null;
    }


    @Override
    public TableOrderCreateResDTO createTableOrder(TableOrderCreateReqDTO tableOrderCreateReqDTO, Zone zone) {
        TableOrder tableOrder = tableOrderCreateReqDTO.toTableOrder(zone);

        if (zone.getId() == null) {
            zoneRepository.save(zone);
        }
        tableOrder.setZone(zone);
        tableOrderRepository.save(tableOrder);
        TableOrderCreateResDTO tableOrderCreateResDTO = tableOrder.toTableOrderCreateResDTO();
        tableOrderCreateResDTO.setId(tableOrder.getId());
        return tableOrderCreateResDTO;
    }

    @Override
    public void changeAllProductToNewTable(Long oldTableId, Long newTableId) {
        Optional<TableOrder> oldTable = this.findById(oldTableId);

        if (!oldTable.isPresent()) {
            throw new DataInputException("bàn không tồn tại, vui lòng kiểm tra lại dữ liệu !!!");
        }

        Optional<TableOrder> newTable = this.findById(newTableId);

        if (!newTable.isPresent()) {
            throw new DataInputException("bàn không tồn tại, vui lòng kiểm tra lại dữ liệu !!!");
        }

        Optional<Order> orderOptional = iOrderService.getByTableOrderAndPaid(oldTable.get(), false);

        if (!orderOptional.isPresent()) {
            throw new DataInputException("đơn hàng không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        Order order = orderOptional.get();

        order.setTableOrder(newTable.get());

        oldTable.get().setStatus(ETableStatus.EMPTY);
        newTable.get().setStatus(ETableStatus.BUSY);

    }

    @Override
    public void combineTable(List<TableOrder> sourceTables, TableOrder targetTable) {
        List<Order> sourceOrders = new ArrayList<>();
        List<OrderDetail> allSourceOrderItems = new ArrayList<>();

        Optional<Order> targetOrderOptional = iOrderService.getByTableOrderAndPaid(targetTable, false);

        if (!targetOrderOptional.isPresent()) {
            throw new DataInputException("Đơn hàng của bàn đích không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        Order targetOrder = targetOrderOptional.get();
        List<OrderDetail> targetOrderItems = iOrderDetailService.getAllByOrder(targetOrder);

        if (targetOrderItems.isEmpty()) {
            throw new DataInputException("Không tìm thấy hóa đơn của bàn đích");
        }

        // kiểm tra và lấy danh sách đơn hàng từ các bàn nguồn
        for (TableOrder sourceTable : sourceTables) {
            Optional<Order> sourceOrderOptional = iOrderService.getByTableOrderAndPaid(sourceTable, false);

            if (!sourceOrderOptional.isPresent()) {
                TableOrderBackup tableBackup = new TableOrderBackup()
                        .setOrderTargetId(targetOrder.getId())
                        .setTableTargetId(targetTable.getId())
                        .setTableCurrentId(sourceTable.getId())
                        .setPaid(false);

                tableOrderBackupRepository.save(tableBackup);

                sourceTable.setStatus(ETableStatus.BUSY);
                tableOrderRepository.save(sourceTable);
            } else {
                Order sourceOrder = sourceOrderOptional.get();
                sourceOrders.add(sourceOrder);
//            BigDecimal sourceOrderAmount = sourceOrderOptional.get().getTotalAmount();

                TableOrderBackup tableBackup = new TableOrderBackup()
                        .setOrderTargetId(targetOrder.getId())
                        .setTableTargetId(targetTable.getId())
                        .setTableCurrentId(sourceTable.getId())
                        .setPaid(false);

                tableOrderBackupRepository.save(tableBackup);

                List<OrderDetail> sourceOrderItems = iOrderDetailService.getAllByOrder(sourceOrder);

                if (sourceOrderItems.isEmpty()) {
                    throw new DataInputException("Không tìm thấy hóa đơn của bàn " + sourceTable.getId());
                }

                allSourceOrderItems.addAll(sourceOrderItems);
            }
        }
        List<OrderDetail> newOrderItems = new ArrayList<>();

        for (OrderDetail sourceItem : allSourceOrderItems) {
            boolean isExist = false;
            int itemIndex = 0;
            for (int i = 0; i < targetOrderItems.size(); i++) {
                OrderDetail targetItem = targetOrderItems.get(i);

                if (Objects.equals(sourceItem.getProduct().getId(), targetItem.getProduct().getId())
                        && Objects.equals(sourceItem.getNote(), targetItem.getNote())
                        && sourceItem.getStatus() == targetItem.getStatus()) {
                    isExist = true;
                    itemIndex = i;
                    break;
                }
            }
            if (isExist) {
                int newQuantity = (int) (sourceItem.getQuantity() + targetOrderItems.get(itemIndex).getQuantity());
                BigDecimal newAmount = sourceItem.getPrice().multiply(BigDecimal.valueOf(newQuantity));

                targetOrderItems.get(itemIndex).setQuantity((long) newQuantity);
                targetOrderItems.get(itemIndex).setAmount(newAmount);
                orderDetailRepository.save(targetOrderItems.get(itemIndex));
            } else {
                sourceItem.setOrder(targetOrder);
                OrderDetail newOrderItem = new OrderDetail().initValue(sourceItem);
                newOrderItem.setId(null);
                newOrderItems.add(newOrderItem);
            }
        }

        tableOrderRepository.save(targetTable);

        BigDecimal newTotalAmount = iOrderService.getOrderTotalAmount(targetOrder.getId());
        targetOrder.setTotalAmount(newTotalAmount);
        orderRepository.save(targetOrder);

        orderDetailRepository.deleteAll(allSourceOrderItems);
        orderRepository.deleteAll(sourceOrders);

        orderDetailRepository.saveAll(newOrderItems);

//        Optional<Order> orderCurrentOptional = iOrderService.getByTableOrderAndPaid(currentTable, false);
//
//        if (!orderCurrentOptional.isPresent()) {
//            throw new DataInputException("đơn hàng hiện tại không hợp lệ, vui lòng kiểm tra lại dữ liệu");
//        }
//
//        Optional<Order> orderTargetOptional = iOrderService.getByTableOrderAndPaid(targetTable, false);
//
//        if (!orderTargetOptional.isPresent()) {
//            throw new DataInputException("đơn hàng muốn gộp không hợp lệ, vui lòng kiểm tra lại dữ liệu");
//        }
//
//        List<OrderDetail> orderItemCurrent = iOrderDetailService.getAllByOrder(orderCurrentOptional.get());
//
//        if (orderItemCurrent.size() == 0) {
//            throw new DataInputException("Không tìm thấy hóa đơn của bàn hiện tại.");
//        }
//
//        List<OrderDetail> orderItemTarget = iOrderDetailService.getAllByOrder(orderTargetOptional.get());
//
//        if (orderItemTarget.size() == 0) {
//            throw new DataInputException("Không tìm thấy hóa đơn của bàn muốn gộp.");
//        }
//
//        TableOrderBackup tableBackup = new TableOrderBackup()
//                .setOrderTargetId(orderCurrentOptional.get().getId())
//                .setOrderCurrentId(orderTargetOptional.get().getId())
//                .setTableCurrentId(targetTable.getId())
//                .setTableTargetId(currentTable.getId());
//
//        tableOrderBackupRepository.save(tableBackup);
//
//        for (OrderDetail itemTarget : orderItemTarget) {
//            billBackupRepository.save(itemTarget.toBillBackup(tableBackup));
//        }
//
//        List<OrderDetail> newOrderItems = new ArrayList<>();
//
//        for (OrderDetail itemCurrent : orderItemCurrent) {
//            boolean isExist = false;
//            int itemIndex = 0;
//            billBackupRepository.save(itemCurrent.toBillBackup(tableBackup));
//
//            for (int i = 0; i < orderItemTarget.size(); i++) {
//                if (Objects.equals(itemCurrent.getProduct().getId(), orderItemTarget.get(i).getProduct().getId())
//                        && Objects.equals(itemCurrent.getNote(), orderItemTarget.get(i).getNote())
//                        && itemCurrent.getStatus() == orderItemTarget.get(i).getStatus()
//                ) {
//                    isExist = true;
//                    itemIndex = i;
//                }
//            }
//
//            if (isExist) {
//                int newQuantity = (int) (itemCurrent.getQuantity() + orderItemTarget.get(itemIndex).getQuantity());
//                BigDecimal newAmount = itemCurrent.getPrice().multiply(BigDecimal.valueOf(newQuantity));
//
//                orderItemTarget.get(itemIndex).setId(orderItemTarget.get(itemIndex).getId());
//                orderItemTarget.get(itemIndex).setQuantity((long) newQuantity);
//                orderItemTarget.get(itemIndex).setAmount(newAmount);
//                orderDetailRepository.save(orderItemTarget.get(itemIndex));
//            } else {
//                itemCurrent.setOrder(orderItemTarget.get(itemIndex).getOrder());
//                OrderDetail newOrderItem = new OrderDetail().initValue(itemCurrent);
//                newOrderItem.setId(null);
//                newOrderItems.add(newOrderItem);
//            }
//
//            isExist = false;
//            itemIndex = 0;
//        }
//
//        currentTable.setStatus(ETableStatus.EMPTY);
//        tableOrderRepository.save(currentTable);
//
//        targetTable.setStatus(ETableStatus.BUSY);
//        tableOrderRepository.save(targetTable);
//
//
//        BigDecimal newTotalAmount = iOrderService.getOrderTotalAmount(orderTargetOptional.get().getId());
//        orderTargetOptional.get().setTotalAmount(newTotalAmount);
//        orderRepository.save(orderTargetOptional.get());
//
//        orderDetailRepository.deleteAll(orderItemCurrent);
//
//        orderRepository.delete(orderCurrentOptional.get());
//
//        orderDetailRepository.saveAll(newOrderItems);
    }

    @Override
    public void combineProduct(List<TableOrder> sourceTables, TableOrder targetTable) {
        List<Order> sourceOrders = new ArrayList<>();
        List<OrderDetail> allSourceOrderItems = new ArrayList<>();

        Optional<Order> targetOrderOptional = iOrderService.getByTableOrderAndPaid(targetTable, false);

        if (!targetOrderOptional.isPresent()) {
            throw new DataInputException("Đơn hàng của bàn đích không hợp lệ, vui lòng kiểm tra lại dữ liệu");
        }

        Order targetOrder = targetOrderOptional.get();
        List<OrderDetail> targetOrderItems = iOrderDetailService.getAllByOrder(targetOrder);

        if (targetOrderItems.isEmpty()) {
            throw new DataInputException("Không tìm thấy hóa đơn của bàn đích");
        }

        // kiểm tra và lấy danh sách đơn hàng từ các bàn nguồn
        for (TableOrder sourceTable : sourceTables) {
            Optional<Order> sourceOrderOptional = iOrderService.getByTableOrderAndPaid(sourceTable, false);
            Order sourceOrder = sourceOrderOptional.get();
            sourceOrders.add(sourceOrder);
            List<OrderDetail> sourceOrderItems = iOrderDetailService.getAllByOrder(sourceOrder);
            if (sourceOrderItems.isEmpty()) {
                throw new DataInputException("Không tìm thấy hóa đơn của bàn " + sourceTable.getId());
            }

            sourceTable.setStatus(ETableStatus.EMPTY);
            tableOrderRepository.save(sourceTable);

            allSourceOrderItems.addAll(sourceOrderItems);
        }

        List<OrderDetail> newOrderItems = new ArrayList<>();

        for (OrderDetail sourceItem : allSourceOrderItems) {
            boolean isExist = false;
            int itemIndex = 0;
            for (int i = 0; i < targetOrderItems.size(); i++) {
                OrderDetail targetItem = targetOrderItems.get(i);

                if (Objects.equals(sourceItem.getProduct().getId(), targetItem.getProduct().getId())
                        && Objects.equals(sourceItem.getNote(), targetItem.getNote())
                        && sourceItem.getStatus() == targetItem.getStatus()) {
                    isExist = true;
                    itemIndex = i;
                    break;
                }
            }
            if (isExist) {
                int newQuantity = (int) (sourceItem.getQuantity() + targetOrderItems.get(itemIndex).getQuantity());
                BigDecimal newAmount = sourceItem.getPrice().multiply(BigDecimal.valueOf(newQuantity));

                targetOrderItems.get(itemIndex).setQuantity((long) newQuantity);
                targetOrderItems.get(itemIndex).setAmount(newAmount);
                orderDetailRepository.save(targetOrderItems.get(itemIndex));
            } else {
                sourceItem.setOrder(targetOrder);
                OrderDetail newOrderItem = new OrderDetail().initValue(sourceItem);
                newOrderItem.setId(null);
                newOrderItems.add(newOrderItem);
            }
        }

        tableOrderRepository.save(targetTable);

        BigDecimal newTotalAmount = iOrderService.getOrderTotalAmount(targetOrder.getId());
        targetOrder.setTotalAmount(newTotalAmount);
        orderRepository.save(targetOrder);

        orderDetailRepository.deleteAll(allSourceOrderItems);
        orderRepository.deleteAll(sourceOrders);

        orderDetailRepository.saveAll(newOrderItems);
    }

    @Override
    public void splitProducts(TableOrder sourceTable, List<ProductSplitReqDTO> products, TableOrder targetTable) {
        Optional<Order> sourceOrderOptional = iOrderService.getByTableOrderAndPaid(sourceTable, false);
        Order sourceOrder = sourceOrderOptional.get();
        List<OrderDetail> sourceOrderDetails = orderDetailRepository.findAllByOrder(sourceOrder);
        List<Long> productArr = new ArrayList<>();
        List<OrderDetail> newSourceOrderDetails = new ArrayList<>();

        int countSourceProduct = sourceOrderDetails.size();
        int countSourceQuantity = 0;

        for (OrderDetail item : sourceOrderDetails) {
            countSourceQuantity += item.getQuantity();
        }

        int countTargetProduct = products.size();
        int countTargetQuantity = 0;

        for (ProductSplitReqDTO item : products) {
            if (item.getQuantity() < 0) {
                throw new DataInputException("Vui long nhap lai so luong san pham > 0");
            }

            if (productArr.contains(item.getId())) {
                throw new DataInputException("Vui long nhap ID san pham khong duoc trung nhau");
            }

            productArr.add(item.getId());
            countTargetQuantity += item.getQuantity();
        }

        if (countSourceProduct == countTargetProduct && countSourceQuantity == countTargetQuantity) {
            throw new DataInputException("Vui long su dung chuc nang chuyen ban");
        }

        if (countSourceQuantity <= countTargetQuantity) {
            throw new DataInputException("Vui long nhap lai so luong san pham muon tach");
        }

        List<Long> sourceProductIdArr = new ArrayList<>();
        List<EOrderDetailStatus> sourceOrderDetailStatuses = new ArrayList<>();
        List<BigDecimal> sourceProductPrice = new ArrayList<>();
        List<Long> sourceQuantityArr = new ArrayList<>();
        List<String> sourceNote = new ArrayList<>();

        for (int i = 0; i < sourceOrderDetails.size(); i++) {
            for (int k = 0; k < products.size(); k++) {
                if (sourceOrderDetails.get(i).getProduct().getId() == products.get(k).getId()) {
                    sourceProductIdArr.add(products.get(k).getId());
                    sourceQuantityArr.add(sourceOrderDetails.get(i).getQuantity() - products.get(k).getQuantity());
                    sourceProductPrice.add(sourceOrderDetails.get(i).getPrice());
                    sourceOrderDetailStatuses.add(sourceOrderDetails.get(i).getStatus());
                    sourceNote.add(sourceOrderDetails.get(i).getNote());
                }
            }
        }


        BigDecimal sourceOrderTotalAmount = BigDecimal.ZERO;

        for (int i = 0; i < sourceProductIdArr.size(); i++) {
            Optional<Product> productOptional = productRepository.findById(sourceProductIdArr.get(i));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(sourceOrderOptional.get());
            orderDetail.setProduct(productOptional.get());
            orderDetail.setQuantity(sourceQuantityArr.get(i));
            orderDetail.setPrice(sourceProductPrice.get(i));
            orderDetail.setAmount(BigDecimal.valueOf(sourceQuantityArr.get(i)).multiply(sourceProductPrice.get(i)));
            orderDetail.setStatus(sourceOrderDetailStatuses.get(i));
            orderDetail.setNote(sourceNote.get(i));

            if (sourceQuantityArr.get(i) > 0) {
                newSourceOrderDetails.add(orderDetail);
            }

            sourceOrderTotalAmount.add(BigDecimal.valueOf(sourceQuantityArr.get(i)).multiply(sourceProductPrice.get(i)));

            orderDetailRepository.deleteOrderDetailByOrderAndProduct(sourceOrderOptional.get(), productOptional.get());
        }

        orderDetailRepository.saveAll(newSourceOrderDetails);

        sourceOrder.setTotalAmount(sourceOrderTotalAmount);
        orderRepository.save(sourceOrder);

        Order targetOrder = new Order();
        targetOrder.setTableOrder(targetTable);
        targetOrder.setPaid(false);
        targetOrder.setTotalAmount(BigDecimal.ZERO);
        targetOrder.setStaff(sourceOrderOptional.get().getStaff());
        orderRepository.save(targetOrder);

        BigDecimal targetOrderTotalAmount = BigDecimal.ZERO;

        for (int i = 0; i < products.size() ; i++) {
            Optional<Product> productOptional = productRepository.findById(products.get(i).getId());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(targetOrder);
            orderDetail.setProduct(productOptional.get());
            orderDetail.setQuantity(products.get(i).getQuantity());
            orderDetail.setPrice(productOptional.get().getPrice());
            orderDetail.setAmount(productOptional.get().getPrice().multiply(BigDecimal.valueOf(products.get(i).getQuantity())));
            orderDetail.setStatus(sourceOrderDetailStatuses.get(i));
            orderDetail.setNote(sourceNote.get(i));
            orderDetailRepository.save(orderDetail);
            targetOrderTotalAmount.add(productOptional.get().getPrice().multiply(BigDecimal.valueOf(products.get(i).getQuantity())));
        }

        targetOrder.setTotalAmount(targetOrderTotalAmount);
        orderRepository.save(targetOrder);

        // Cập nhật trạng thái của bàn đích
        targetTable.setStatus(ETableStatus.BUSY);
        tableOrderRepository.save(targetTable);



    }

    private BigDecimal calculateTotalAmount(List<OrderDetail> orderItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderDetail item : orderItems) {
            BigDecimal itemTotalAmount = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotalAmount);
        }
        return totalAmount;
    }




    @Override
    public List<TableOrderDTO> findAllTableOrder() {
        return tableOrderRepository.findAllTableOrder();
    }

    @Override
    public TableOrderCountDTO countTable() {
        return tableOrderRepository.countTable();
    }

    @Override
    public List<TableOrderWithZoneCountDTO> countTableOrderByZone() {
        return tableOrderRepository.countTableOrderByZone();
    }

    @Override
    public Boolean existsByTitle(String title) {
        return tableOrderRepository.existsByTitle(title);
    }


}
