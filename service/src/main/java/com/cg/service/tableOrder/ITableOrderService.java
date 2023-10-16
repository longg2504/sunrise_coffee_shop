package com.cg.service.tableOrder;

import com.cg.domain.dto.tableOrder.TableOrderCreateReqDTO;
import com.cg.domain.dto.tableOrder.TableOrderCreateResDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITableOrderService extends IGeneralService<TableOrder, Long> {
    Page<TableOrderDTO> findAllTableOrder(String search, Pageable pageable);

    List<TableOrderDTO> findAllTablesWithoutSenderId(@Param("tableId") Long tableId);

    TableOrderCreateResDTO createTableOrder(TableOrderCreateReqDTO tableOrderCreateReqDTO, Zone zone);

}
