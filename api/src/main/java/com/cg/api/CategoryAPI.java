package com.cg.api;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.entity.Category;
import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.category.ICategoryService;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryAPI {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ValidateUtils validateUtils;
    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDTO> categoryDTOS = categoryService.findAllCategoryDTO();
        if (categoryDTOS.isEmpty()) {
            throw new ResourceNotFoundException("Không có danh mục nào vui lòng kiểm tra lại hệ thống");
        }
        return new ResponseEntity<>(categoryDTOS,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getByIdCategory(@PathVariable("categoryId") String categoryIdStr) {

        if (!validateUtils.isNumberValid(categoryIdStr)) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }
        Long categoryId = Long.parseLong(categoryIdStr);

        Category category = categoryService.findByIdAndDeletedFalse(categoryId).orElseThrow(() -> {
            throw new DataInputException("Mã danh mục không tồn tại");
        });

        CategoryDTO categoryDTO = category.toCategoryDTO();
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);

    }
}
