package com.cg.service.billBackup;

import com.cg.domain.entity.BillBackup;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IBillBackupService extends IGeneralService<BillBackup, Long> {
    Optional<BillBackup> findByTableId(Long tableId);
    List<BillBackup> findAllByOrderId(Long orderId);
}


