package com.cg.service.tableOrder;

import com.cg.domain.dto.tableOrder.*;
import com.cg.domain.entity.*;
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
