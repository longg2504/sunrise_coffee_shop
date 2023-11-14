package com.cg.repository.tableOrder;

import com.cg.domain.dto.tableOrder.TableOrderCountDTO;
import com.cg.domain.dto.tableOrder.TableOrderDTO;
import com.cg.domain.dto.tableOrder.TableOrderWithZoneCountDTO;
import com.cg.domain.entity.TableOrder;
import com.cg.domain.entity.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "FROM TableOrder AS to " +
            "WHERE to.deleted = false")
    List<TableOrderDTO> findAllTableOrder();

    @Query("SELECT NEW com.cg.domain.dto.tableOrder.TableOrderDTO (" +
            "to.id, " +
            "to.title, " +
            "to.status, " +
            "to.zone " +
            ") " +
            "FROM TableOrder AS to " +
            "WHERE to.title LIKE %:search%")
    Page<TableOrderDTO> findAllTableOrderByTitle(@Param("search") String search, Pageable pageable);


    @Query("SELECT NEW com.cg.domain.dto.tableOrder.TableOrderCountDTO (" +
            "count(tb.id) " +
            ") " +
            "FROM TableOrder AS tb " +
            "WHERE tb.deleted = false "
    )
    TableOrderCountDTO countTable ();

    @Query("SELECT NEW com.cg.domain.dto.tableOrder.TableOrderWithZoneCountDTO(" +
            "count(tb.id)," +
            "z.title " +
            ") " +
            "FROM TableOrder AS tb " +
            "JOIN Zone AS z " +
            "ON tb.zone.id = z.id " +
            "WHERE tb.deleted = false " +
            "GROUP BY z.title "

    )
    List<TableOrderWithZoneCountDTO> countTableOrderByZone();

}
