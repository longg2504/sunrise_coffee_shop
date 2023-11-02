package com.cg.service.tableOrderBackup;

import com.cg.domain.entity.TableOrderBackup;
import com.cg.service.IGeneralService;

import java.util.Optional;

public interface ITableOrderBackupService extends IGeneralService<TableOrderBackup, Long> {
    Optional<TableOrderBackup> findByTableCurrentIdAndOrderCurrentId(Long tableCurrentId, Long orderCurrentId);

    Optional<TableOrderBackup> findByTableTargetId( Long tableTargetId);

    Optional<TableOrderBackup> findByTableCurrentId(Long tableCurrentId);

    Optional<TableOrderBackup> findByOrderCurrentId(Long orderCurrentId);

}
