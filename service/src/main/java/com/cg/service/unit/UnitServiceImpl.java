package com.cg.service.unit;

import com.cg.domain.entity.Unit;
import com.cg.repository.unit.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UnitServiceImpl implements IUnitService{
    @Autowired
    private UnitRepository unitRepository;
    @Override
    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    @Override
    public Optional<Unit> findById(Long id) {
        return unitRepository.findById(id);
    }

    @Override
    public Unit save(Unit unit) {
        return unitRepository.save(unit);
    }

    @Override
    public void delete(Unit unit) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<Unit> findByIdAndDeletedFalse(Long id) {
        return unitRepository.findByIdAndDeletedFalse(id);
    }
}
