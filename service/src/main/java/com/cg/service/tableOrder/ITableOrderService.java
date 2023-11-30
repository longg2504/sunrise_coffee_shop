package com.cg.service.tableOrder;

import com.cg.domain.dto.product.DataSplitReqDTO;
import com.cg.domain.dto.product.ProductSplitReqDTO;
import com.cg.domain.dto.tableOrder.*;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import com.cg.service.IGeneralService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITableOrderService extends IGeneralService<TableOrder, Long> {
    Page<TableOrderDTO> findAllTableOrder(Zone zone, String status, String search, Pageable pageable);

    List<TableOrderDTO> findAllTablesWithoutSenderId(@Param("tableId") Long tableId);


    TableOrderCreateResDTO createTableOrder(TableOrderCreateReqDTO tableOrderCreateReqDTO, Zone zone);

    void changeAllProductToNewTable(Long oldTableId, Long newTableId);

    void combineTable(List<TableOrder> sourceTables, TableOrder targetTable);

    void combineProduct(List<TableOrder> sourceTables, TableOrder targetTable);

//    TableOrder unCombineTable(TableOrder currentTable);

    List<TableOrderDTO> findAllTableOrder();

    TableOrderCountDTO countTable ();

    List<TableOrderWithZoneCountDTO> countTableOrderByZone();
    Boolean existsByTitle(String title);


    void splitProducts(TableOrder sourceTable, List<ProductSplitReqDTO> products, TableOrder tagretTable);
}
