package com.cg.api;

import com.cg.domain.dto.product.*;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.Unit;
import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.category.ICategoryService;
import com.cg.service.product.IProductService;
import com.cg.service.unit.IUnitService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private ValidateUtils validateUtils;
    @Autowired
    private AppUtils appUtils;


    @GetMapping
    public ResponseEntity<?> getAllProduct(@RequestParam (defaultValue = "") String search, Pageable pageable) {
        Page<ProductDTO> productDTOS = productService.findProductByName(search,pageable);
        if (productDTOS.isEmpty()) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getByIdProduct(@PathVariable("productId") String productIdStr) {
        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }
        Long productId = Long.valueOf(productIdStr);

        Product product = productService.findByIdAndDeletedFalse(productId).orElseThrow(() -> {
            throw new DataInputException("Mã sản phẩm không tồn tại");
        });
        ProductDTO productDTO = product.toProductDTO();
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createProduct(@ModelAttribute ProductCreReqDTO productCreReqDTO, BindingResult bindingResult) {
        new ProductCreReqDTO().validate(productCreReqDTO, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (productCreReqDTO.getProductAvatar() == null) {
            throw new DataInputException("Hình ảnh sản phẩm không được để trống");
        }
        if (!validateUtils.isNumberValid(productCreReqDTO.getCategoryId())) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }

        Long idCategory = Long.parseLong(productCreReqDTO.getCategoryId());
        Category category = categoryService.findByIdAndDeletedFalse(idCategory).orElseThrow(() -> {
            throw new DataInputException("Mã danh mục không tồn tại");
        });

        Long idUnit = Long.valueOf(productCreReqDTO.getUnitId());
        Unit unit = unitService.findByIdAndDeletedFalse(idUnit).orElseThrow(() -> {
            throw new DataInputException("Mã đơn vị không tồn tại");
        });

        Product product = productService.createProduct(productCreReqDTO, category, unit);
        ProductCreResDTO productCreResDTO = product.toProductCreResDTO();
        return new ResponseEntity<>(productCreResDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productIdStr, @ModelAttribute ProductUpReqDTO productUpReqDTO, BindingResult bindingResult) {
        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }
        Long productId = Long.valueOf(productIdStr);
        Optional<Product> productOptional = productService.findByIdAndDeletedFalse(productId);
        if (productOptional.isEmpty()) {
            throw new DataInputException("Mã sản phẩm không tồn tại");
        }
        if (!validateUtils.isNumberValid(productUpReqDTO.getCategoryId())) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }
        Long idCategory = Long.parseLong(productUpReqDTO.getCategoryId());
        Category category = categoryService.findByIdAndDeletedFalse(idCategory).orElseThrow(() -> {
            throw new DataInputException("Mã danh mục không tồn tại");
        });
        Long idUnit = Long.parseLong(productUpReqDTO.getUnitId());
        Unit unit = unitService.findByIdAndDeletedFalse(idUnit).orElseThrow(() -> {
            throw new DataInputException("Mã đơn vị không tồn tại");
        });
        if (productUpReqDTO.getProductAvatar() == null) {
            Product product = productUpReqDTO.toProductChangeImage(unit, category);
            product.setId(productOptional.get().getId());
            product.setProductAvatar(productOptional.get().getProductAvatar());
            productService.save(product);
            return new ResponseEntity<>(product.toProductUpResDTO(), HttpStatus.OK);
        } else {
            Product productUpdate = productService.update(productId, productUpReqDTO, category, unit);
            ProductUpResDTO productUpResDTO = productUpdate.toProductUpResDTO();
            return new ResponseEntity<>(productUpResDTO, HttpStatus.OK);
        }

    }
    @DeleteMapping("/{productId}")
    public  ResponseEntity<?> deleteProduct(@PathVariable("productId") String productIdStr) {
        if(!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }
        Long productId = Long.parseLong(productIdStr);
        Product product = productService.findByIdAndDeletedFalse(productId).orElseThrow(()-> {
            throw new DataInputException("Mã sản phẩm không tồn tại");
        });
        productService.deleteByIdTrue(product);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
