package com.cg.service.category;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.entity.Category;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICategoryService extends IGeneralService<Category,Long> {
    List<CategoryDTO> findAllCategoryDTO();
    Optional<Category> findByIdAndDeletedFalse(Long id);

}
