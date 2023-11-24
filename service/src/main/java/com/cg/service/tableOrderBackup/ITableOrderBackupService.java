package com.cg.service.tableOrderBackup;

import com.cg.domain.entity.TableOrderBackup;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITableOrderBackupService extends IGeneralService<TableOrderBackup, Long> {
//    Optional<TableOrderBackup> findByTableCurrentIdAndOrderCurrentId(Long tableCurrentId, Long orderCurrentId);

    Optional<TableOrderBackup> findByTableTargetId( Long tableTargetId);

    Optional<TableOrderBackup> findByTableCurrentId(Long tableCurrentId);

    List<TableOrderBackup> findByOrderTargetId(Long orderTargetId);
}
