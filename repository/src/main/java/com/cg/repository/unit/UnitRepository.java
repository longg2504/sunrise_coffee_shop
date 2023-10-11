package com.cg.repository.unit;

import com.cg.domain.entity.Category;
import com.cg.domain.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit,Long> {
    Optional<Unit> findByIdAndDeletedFalse(Long id);
}
