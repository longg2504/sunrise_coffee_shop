package com.cg.repository.category;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT NEW com.cg.domain.dto.category.CategoryDTO(ca.id,ca.title) from Category as ca WHERE ca.deleted=false ")
    List<CategoryDTO> findAllCategoryDTO();

    Optional<Category> findByIdAndDeletedFalse(Long id);
}
