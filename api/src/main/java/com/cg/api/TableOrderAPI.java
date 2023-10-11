package com.cg.api;

import com.cg.domain.dto.tableOrder.TableOrderCreateReqDTO;
import com.cg.domain.dto.tableOrder.TableOrderCreateResDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.tableOrder.ITableOrderService;
import com.cg.service.zone.IZoneService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tableOrders")
public class TableOrderAPI {
    @Autowired
    private ITableOrderService tableOrderService;

    @Autowired
    private IZoneService zoneService;

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
        if (tableOrderOptional.isPresent()) {
            throw new DataInputException("Bàn này không tồn tại !");
        }

        return new ResponseEntity<>(tableOrderOptional, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTable(@RequestBody TableOrderCreateReqDTO tableOrderCreateReqDTO) {
        Long isZone = Long.parseLong(tableOrderCreateReqDTO.getZoneId());
        Zone zone = zoneService.findByIdAndDeletedFalse(isZone).orElseThrow(() -> new DataInputException("Khu vực không tồn tại"));
        TableOrderCreateResDTO tableOrderCreateResDTO = tableOrderService.createTableOrder(tableOrderCreateReqDTO, zone);
        return new ResponseEntity<>(tableOrderCreateResDTO, HttpStatus.OK);
    }
}
