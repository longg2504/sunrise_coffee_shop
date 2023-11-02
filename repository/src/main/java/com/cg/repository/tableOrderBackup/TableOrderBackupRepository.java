package com.cg.repository.tableOrderBackup;

import com.cg.domain.entity.TableOrderBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableOrderBackupRepository extends JpaRepository<TableOrderBackup, Long> {
    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.orderCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.tableCurrentId = :tableCurrentId " +
            "AND tbu.orderCurrentId = :orderCurrentId "
    )
    Optional<TableOrderBackup> findByTableCurrentIdAndOrderCurrentId(@Param("tableCurrentId") Long tableCurrentId, @Param("orderCurrentId") Long orderCurrentId);

    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.orderCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.tableTargetId = :tableTargetId "
    )
    Optional<TableOrderBackup> findByTableTargetId(@Param("tableTargetId") Long tableTargetId);

    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup(" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.orderCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.tableCurrentId = :tableCurrentId "
    )
    Optional<TableOrderBackup> findByTableCurrentId(@Param("tableCurrentId") Long tableCurrentId);


    @Query("SELECT NEW com.cg.domain.entity.TableOrderBackup (" +
            "tbu.id, " +
            "tbu.tableCurrentId, " +
            "tbu.orderCurrentId, " +
            "tbu.tableTargetId," +
            "tbu.orderTargetId " +
            ") " +
            "FROM TableOrderBackup AS tbu " +
            "WHERE tbu.orderCurrentId = :orderCurrentId "
    )
    Optional<TableOrderBackup> findByOrderCurrentId(@Param("orderCurrentId") Long orderCurrentId);

}
