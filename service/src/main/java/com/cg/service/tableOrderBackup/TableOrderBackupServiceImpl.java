package com.cg.service.tableOrderBackup;

import com.cg.domain.entity.TableOrderBackup;
import com.cg.repository.tableOrderBackup.TableOrderBackupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TableOrderBackupServiceImpl implements ITableOrderBackupService{

    @Autowired
    private TableOrderBackupRepository tableOrderBackupRepository;


    @Override
    public List<TableOrderBackup> findAll() {
        return tableOrderBackupRepository.findAll();
    }

    @Override
    public Optional<TableOrderBackup> findById(Long id) {
        return tableOrderBackupRepository.findById(id);
    }

    @Override
    public TableOrderBackup save(TableOrderBackup tableOrderBackup) {
        return tableOrderBackupRepository.save(tableOrderBackup);
    }

    @Override
    public void delete(TableOrderBackup tableOrderBackup) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<TableOrderBackup> findByTableCurrentIdAndOrderCurrentId(Long tableCurrentId, Long orderCurrentId) {
        return tableOrderBackupRepository.findByTableCurrentIdAndOrderCurrentId(tableCurrentId, orderCurrentId);
    }

    @Override
    public Optional<TableOrderBackup> findByTableTargetId(Long tableTargetId) {
        return tableOrderBackupRepository.findByTableTargetId(tableTargetId);
    }

    @Override
    public Optional<TableOrderBackup> findByTableCurrentId(Long tableCurrentId) {
        return tableOrderBackupRepository.findByTableCurrentId(tableCurrentId);
    }

    @Override
    public Optional<TableOrderBackup> findByOrderCurrentId(Long orderCurrentId) {
        return tableOrderBackupRepository.findByOrderCurrentId(orderCurrentId);
    }
}
