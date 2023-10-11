package com.cg.repository.supplierIngredient;

import com.cg.domain.entity.SupplierIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierIngredientRepository extends JpaRepository<SupplierIngredient,Long> {
}
