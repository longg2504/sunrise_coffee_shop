package com.cg.repository.tableOrder;

import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableOrderRepository extends JpaRepository<TableOrder, Long> {

    @Query("SELECT NEW com.cg.domain.dto.tableOrder.TableOrderDTO (" +
            "to.id, " +
            "to.title, " +
            "to.status, " +
            "to.zone" +
            ")" +
            "FROM TableOrder AS to")
    List<TableOrderDTO> findAllTableOrder();

    @Query("SELECT NEW com.cg.domain.dto.tableOrder.TableOrderDTO (" +
            "to.id, " +
            "to.title, " +
            "to.status, " +
            "to.zone " +
            ") " +
            "FROM TableOrder AS to " +
            "WHERE to.title LIKE %:search%")
    List<TableOrderDTO> findAllTableOrderByTitle(@Param("search") String search);
}
