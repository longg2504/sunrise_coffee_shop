package com.cg.service.billBackup;

import com.cg.domain.entity.BillBackup;
import com.cg.repository.billBackUp.BillBackupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BillBackupServiceImpl implements IBillBackupService{
    @Autowired
    private BillBackupRepository backupBillRepository;


    @Override
    public List<BillBackup> findAll() {
        return backupBillRepository.findAll();
    }

    @Override
    public Optional<BillBackup> findById(Long id) {
        return backupBillRepository.findById(id);
    }

    @Override
    public BillBackup save(BillBackup billBackup) {
        return backupBillRepository.save(billBackup);
    }

    @Override
    public void delete(BillBackup billBackup) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<BillBackup> findByTableId(Long tableId) {
        return backupBillRepository.findByTableId(tableId);
    }

    @Override
    public List<BillBackup> findAllByOrderId(Long orderId) {
        return backupBillRepository.findAllByOrderId(orderId);
    }
}
