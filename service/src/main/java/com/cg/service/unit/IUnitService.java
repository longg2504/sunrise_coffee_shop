package com.cg.service.unit;

import com.cg.domain.entity.Unit;
import com.cg.service.IGeneralService;

import java.util.Optional;

public interface IUnitService extends IGeneralService<Unit,Long> {
    Optional<Unit> findByIdAndDeletedFalse(Long id);

}
