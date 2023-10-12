package com.cg.service.tableOrder;

import com.cg.domain.dto.tableOrder.TableOrderCreateReqDTO;
import com.cg.domain.dto.tableOrder.TableOrderCreateResDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import com.cg.repository.tableOrder.TableOrderRepository;
import com.cg.repository.zone.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableOrderServiceImpl implements ITableOrderService {

    @Autowired
    private TableOrderRepository tableOrderRepository;

    @Autowired
    private ZoneRepository zoneRepository;
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
    public List<TableOrderDTO> findAllTableOrder(String search) {
        return tableOrderRepository.findAllTableOrderByTitle(search);

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
}
