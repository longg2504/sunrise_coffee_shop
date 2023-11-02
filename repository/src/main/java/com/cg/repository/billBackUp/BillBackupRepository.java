package com.cg.repository.billBackUp;

import com.cg.domain.entity.BillBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BillBackupRepository extends JpaRepository<BillBackup, Long> {

    Optional<BillBackup> findByTableId(Long tableId);

    List<BillBackup> findAllByOrderId(Long orderId);


}
