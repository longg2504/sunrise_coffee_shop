package com.cg.repository.tableOrderBackup;

import com.cg.domain.entity.TableOrderBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableOrderBackupRepository extends JpaRepository<TableOrderBackup, Long> {
    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId, " +
            "tbu.paid " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.tableCurrentId = :tableCurrentId " +
            "AND tbu.paid = false "
    )
    Optional<TableOrderBackup> findByTableCurrentIdAndOrderCurrentId(@Param("tableCurrentId") Long tableCurrentId);

    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId," +
            "tbu.paid " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.tableTargetId = :tableTargetId " +
            "AND tbu.paid = false "
    )
    Optional<TableOrderBackup> findByTableTargetId(@Param("tableTargetId") Long tableTargetId);

    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup(" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId," +
            "tbu.paid " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.tableCurrentId = :tableCurrentId " +
            "AND tbu.paid = false "
    )
    Optional<TableOrderBackup> findByTableCurrentId(@Param("tableCurrentId") Long tableCurrentId);


    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId," +
            "tbu.paid " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.orderTargetId = :orderTargetId " +
            "AND tbu.paid = false "
    )
    List<TableOrderBackup> findByOrderTargetId(@Param("orderTargetId") Long orderTargetId);

}
