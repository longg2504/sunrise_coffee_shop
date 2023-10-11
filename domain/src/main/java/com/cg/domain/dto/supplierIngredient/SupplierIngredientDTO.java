package com.cg.domain.dto.supplierIngredient;

import com.cg.domain.entity.Ingredient;
import com.cg.domain.entity.Supplier;
import com.cg.domain.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class SupplierIngredientDTO {
    private Long id;
    private Long quantity;
    private Supplier supplier;
    private Ingredient ingredient;
    private Unit unit;
}
