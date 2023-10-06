package com.cg.api;

import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.entity.TableOrder;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.tableOrder.ITableOrderService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tableOrders")
public class TableOrderAPI {
    @Autowired
    private ITableOrderService tableOrderService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @GetMapping
    public ResponseEntity<?> getAllTableOrder() {
        List<TableOrderDTO> tableOrderDTO = tableOrderService.findAllTableOrder();
        if (tableOrderDTO.isEmpty()) {
            throw new ResourceNotFoundException("Không có bàn nào vui lòng kiểm tra lại hệ thống");
        }
        return new ResponseEntity<>(tableOrderDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTableOrderById(@PathVariable Long id) {
        Optional<TableOrder> tableOrderOptional = tableOrderService.findById(id);

    }
}
