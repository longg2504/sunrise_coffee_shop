package com.cg.api;

import com.cg.domain.dto.tableOrder.TableOrderCreateReqDTO;
import com.cg.domain.dto.tableOrder.TableOrderCreateResDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.dto.tableOrder.TableOrderWithZoneCountDTO;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import com.cg.domain.enums.ETableStatus;
import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.tableOrder.ITableOrderService;
import com.cg.service.zone.IZoneService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/all")
    public ResponseEntity<?> getAllTableOrder() {
        List<TableOrderDTO> tableOrderDTO = tableOrderService.findAllTableOrder();

        if (tableOrderDTO.isEmpty()) {
            throw new ResourceNotFoundException("Không có bàn nào vui lòng kiểm tra lại hệ thống");
        }
        return new ResponseEntity<>(tableOrderDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllTableOrder(@RequestParam(defaultValue = "") Zone zone,
                                              @RequestParam(defaultValue = "") String status,
                                              @RequestParam(defaultValue = "") String search,
                                              Pageable pageable) {
        Page<TableOrderDTO> tableOrderDTO = tableOrderService.findAllTableOrder(zone,status,search, pageable);

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

    @GetMapping("/get-count-table-by-zone")
    public ResponseEntity<?> getCountTableByZone(){
        List<TableOrderWithZoneCountDTO> tableOrderWithZoneCountDTO = tableOrderService.countTableOrderByZone();
        return new ResponseEntity<>(tableOrderWithZoneCountDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTable(@RequestBody TableOrderCreateReqDTO tableOrderCreateReqDTO) {
        Boolean existsByTitle = tableOrderService.existsByTitle(tableOrderCreateReqDTO.getTitle());

        if (existsByTitle) {
            throw new EmailExistsException("Bàn này đã tồn tại vui lòng xem lại!!!");
        }
        Long isZone = Long.parseLong(tableOrderCreateReqDTO.getZoneId());
        Zone zone = zoneService.findByIdAndDeletedFalse(isZone).orElseThrow(() -> new DataInputException("Khu vực không tồn tại"));
        TableOrderCreateResDTO tableOrderCreateResDTO = tableOrderService.createTableOrder(tableOrderCreateReqDTO, zone);
        return new ResponseEntity<>(tableOrderCreateResDTO, HttpStatus.OK);
    }

    @PostMapping("/change-table")
    public ResponseEntity<?> changeAllProductToNewTable(HttpServletRequest request) throws IOException {


        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        String oldTableIdStr;
        String newTableIdStr;

        try {
            oldTableIdStr = json.get("oldTableId").asText();
            newTableIdStr = json.get("newTableId").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        if (!validateUtils.isNumberValid(oldTableIdStr)) {
            throw new DataInputException("ID bàn cũ phải là ký tự số");
        }

        if (!validateUtils.isNumberValid(newTableIdStr)) {
            throw new DataInputException("ID bàn mới phải là ký tự số");
        }

        Long oldTableId = Long.parseLong(oldTableIdStr);

        Long newTableId = Long.parseLong(newTableIdStr);

        Optional<TableOrder> optionalAppTable = tableOrderService.findById(newTableId);

        if (!optionalAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TableOrder newAppTable = optionalAppTable.get();

        ETableStatus eTableStatus = ETableStatus.fromString(newAppTable.getStatus().toString().toUpperCase());

        if (!eTableStatus.equals(ETableStatus.EMPTY)) {
            throw new DataInputException("Trạng thái bàn mới không hợp lệ, vui lòng kiểm tra lại.");
        }


        tableOrderService.changeAllProductToNewTable(oldTableId, newTableId);


        List<TableOrderDTO> tableDTOs = tableOrderService.findAllTableOrder();

        return new ResponseEntity<>(tableDTOs, HttpStatus.OK);
    }


    @PostMapping("/combine-tables")
    public ResponseEntity<?> combineTables(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        List<String> currentTableIds;
        String targetTableIdStr;

        try {
            currentTableIds = mapper.convertValue(json.get("currentTableIds"), new TypeReference<List<String>>() {});
            targetTableIdStr = json.get("targetTableId").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        for (String currentTableIdStr : currentTableIds) {
            if (!validateUtils.isNumberValid(currentTableIdStr)) {
                throw new DataInputException("ID bàn hiện tại phải là ký tự số");
            }
        }

        if (!validateUtils.isNumberValid(targetTableIdStr)) {
            throw new DataInputException("ID bàn mục tiêu phải là ký tự số");
        }

        Long targetTableId = Long.parseLong(targetTableIdStr);

        Optional<TableOrder> optionalTargetAppTable = tableOrderService.findById(targetTableId);

        if (!optionalTargetAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TableOrder targetTable = optionalTargetAppTable.get();

        List<TableOrder> sourceTables = new ArrayList<>();
        for (String currentTableIdStr : currentTableIds) {
            Long currentTableId = Long.parseLong(currentTableIdStr);
            Optional<TableOrder> optionalCurrentAppTable = tableOrderService.findById(currentTableId);

            if (!optionalCurrentAppTable.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            TableOrder currentTable = optionalCurrentAppTable.get();
            sourceTables.add(currentTable);
        }

        try {
            tableOrderService.combineTable(sourceTables, targetTable);
        } catch (DataInputException e) {
            throw new DataInputException(e.getMessage());
        }

        TableOrderDTO targetTableDTO = targetTable.toTableOrderDTO();

        Map<String, TableOrderDTO> result = new HashMap<>();
        result.put("targetTable", targetTableDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/combine-products")
    public ResponseEntity<?> combineProducts(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new JsonMapper();
        JsonNode json = mapper.readTree(body);

        List<String> currentTableIds;
        String targetTableIdStr;

        try {
            currentTableIds = mapper.convertValue(json.get("currentTableIds"), new TypeReference<List<String>>() {});
            targetTableIdStr = json.get("targetTableId").asText();
        } catch (Exception e) {
            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
        }

        for (String currentTableIdStr : currentTableIds) {
            if (!validateUtils.isNumberValid(currentTableIdStr)) {
                throw new DataInputException("ID bàn hiện tại phải là ký tự số");
            }
        }

        if (!validateUtils.isNumberValid(targetTableIdStr)) {
            throw new DataInputException("ID bàn mục tiêu phải là ký tự số");
        }

        Long targetTableId = Long.parseLong(targetTableIdStr);

        Optional<TableOrder> optionalTargetAppTable = tableOrderService.findById(targetTableId);

        if (!optionalTargetAppTable.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TableOrder targetTable = optionalTargetAppTable.get();

        List<TableOrder> sourceTables = new ArrayList<>();
        for (String currentTableIdStr : currentTableIds) {
            Long currentTableId = Long.parseLong(currentTableIdStr);
            Optional<TableOrder> optionalCurrentAppTable = tableOrderService.findById(currentTableId);

            if (!optionalCurrentAppTable.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            TableOrder currentTable = optionalCurrentAppTable.get();
            sourceTables.add(currentTable);
        }

        try {
            tableOrderService.combineProduct(sourceTables, targetTable);
        } catch (DataInputException e) {
            throw new DataInputException(e.getMessage());
        }

        TableOrderDTO targetTableDTO = targetTable.toTableOrderDTO();

        Map<String, TableOrderDTO> result = new HashMap<>();
        result.put("targetTable", targetTableDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @PostMapping("/combine-tables")
//    public ResponseEntity<?> combineTables(HttpServletRequest request) throws IOException {
//        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//        ObjectMapper mapper = new JsonMapper();
//        JsonNode json = mapper.readTree(body);
//
//        String currentTableIdStr;
//        List<String> targetTableIdsStr = new ArrayList<>();
//
//        try {
//            currentTableIdStr = json.get("currentTableId").asText();
//            JsonNode targetTableIdsNode = json.get("targetTableIds");
//            if (targetTableIdsNode.isArray()) {
//                for (JsonNode idNode : targetTableIdsNode) {
//                    targetTableIdsStr.add(idNode.asText());
//                }
//            }
//        } catch (Exception e) {
//            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
//        }
//
//        if (!validateUtils.isNumberValid(currentTableIdStr)) {
//            throw new DataInputException("ID bàn hiện tại phải là ký tự số");
//        }
//
//        List<Long> targetTableIds = new ArrayList<>();
//        for (String targetTableIdStr : targetTableIdsStr) {
//            if (!validateUtils.isNumberValid(targetTableIdStr)) {
//                throw new DataInputException("ID bàn mục tiêu phải là ký tự số");
//            }
//            targetTableIds.add(Long.parseLong(targetTableIdStr));
//        }
//
//        Long currentTableId = Long.parseLong(currentTableIdStr);
//
//        Optional<TableOrder> optionalCurrentAppTable = tableOrderService.findById(currentTableId);
//
//        if (!optionalCurrentAppTable.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        TableOrder currentTable = optionalCurrentAppTable.get();
//
//        ETableStatus eCurrentTableStatus = ETableStatus.fromString(currentTable.getStatus().toString().toUpperCase());
//
//        if (!eCurrentTableStatus.equals(ETableStatus.BUSY)) {
//            throw new DataInputException("Trạng thái bàn hiện tại không hợp lệ, vui lòng kiểm tra lại.");
//        }
//
//        Map<String, TableOrderDTO> result = new HashMap<>();
//
//        for (Long targetTableId : targetTableIds) {
//            if (!validateUtils.isNumberValid(targetTableId.toString())) {
//                throw new DataInputException("ID bàn mục tiêu phải là ký tự số");
//            }
//
//            Optional<TableOrder> optionalTargetAppTable = tableOrderService.findById(targetTableId);
//
//            if (!optionalTargetAppTable.isPresent()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            TableOrder targetTable = optionalTargetAppTable.get();
//            ETableStatus eTargetTableStatus = ETableStatus.fromString(targetTable.getStatus().toString().toUpperCase());
//
//            if (!eTargetTableStatus.equals(ETableStatus.BUSY)) {
//                throw new DataInputException("Trạng thái bàn muốn gộp không hợp lệ, vui lòng kiểm tra lại.");
//            }
//
//            tableOrderService.combineTable(currentTable, targetTable);
//
//            TableOrderDTO targetTableDTO = targetTable.toTableOrderDTO();
//            result.put("targetTable" + targetTableId, targetTableDTO);
//        }
//
//        TableOrderDTO currentTableDTO = currentTable.toTableOrderDTO();
//        result.put("currentTable", currentTableDTO);
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }


//    @PostMapping("/un-combine-tables")
//    public ResponseEntity<?> unCombineTables(HttpServletRequest request) throws IOException {
//
//        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//
//        ObjectMapper mapper = new JsonMapper();
//        JsonNode json = mapper.readTree(body);
//
//        String currentTableIdStr;
//
//        try {
//            currentTableIdStr = json.get("currentTableId").asText();
//        } catch (Exception e) {
//            throw new DataInputException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin");
//        }
//
//        if (!validateUtils.isNumberValid(currentTableIdStr)) {
//            throw new DataInputException("ID bàn hiện tại phải là ký tự số");
//        }
//
//        Long currentTableId = Long.parseLong(currentTableIdStr);
//
//        Optional<TableOrder> optionalCurrentAppTable = tableOrderService.findById(currentTableId);
//
//        if (!optionalCurrentAppTable.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        TableOrder currentTable = optionalCurrentAppTable.get();
//
//        ETableStatus eCurrentTableStatus = ETableStatus.fromString(currentTable.getStatus().toString().toUpperCase());
//
//        if (!eCurrentTableStatus.equals(ETableStatus.BUSY)) {
//            throw new DataInputException("Trạng thái bàn hiện tại không hợp lệ, vui lòng kiểm tra lại.");
//        }
//
//        TableOrder targetTable = tableOrderService.unCombineTable(currentTable);
//
//        TableOrderDTO currentTableDTO = currentTable.toTableOrderDTO();
//
//        TableOrderDTO targetTableDTO = targetTable.toTableOrderDTO();
//
//        Map<String, TableOrderDTO> result = new HashMap<>();
//        result.put("currentTable", currentTableDTO);
//        result.put("targetTable", targetTableDTO);
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

}

