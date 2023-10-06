package com.cg.repository.tableOrder;

import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.entity.TableOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableOrderRepository extends JpaRepository<TableOrder, Long> {

    List<TableOrderDTO> findAllTableOrder();

}
