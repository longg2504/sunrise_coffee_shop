package com.cg.api;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryAPI {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        List<CategoryDTO> categoryDTOS = categoryService.findAllCategoryDTO();
        return new ResponseEntity<>(categoryDTOS,HttpStatus.OK);
    }
}
