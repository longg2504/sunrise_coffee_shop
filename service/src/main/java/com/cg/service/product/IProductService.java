package com.cg.service.product;

import com.cg.domain.dto.product.ProductCountDTO;
import com.cg.domain.dto.product.ProductCreReqDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.product.ProductUpReqDTO;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.Unit;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProductService extends IGeneralService<Product,Long> {
    List<ProductDTO> findAllProductDTO();

    Product createProduct(ProductCreReqDTO productCreReqDTO, Category category, Unit unit);
    Optional<Product> findByIdAndDeletedFalse(Long id);

    Product update(Long productId, ProductUpReqDTO productUpReqDTO, Category category,Unit unit);

    void deleteByIdTrue(Product product);
    Page<ProductDTO> findProductByName(String keySearch, Pageable pageable);

    List<ProductDTO> findAllByDeletedFalse(Sort sort);
    Page<ProductDTO> findProductByKeySearch(String keySearch, Pageable pageable);

    ProductCountDTO countProduct();
}
