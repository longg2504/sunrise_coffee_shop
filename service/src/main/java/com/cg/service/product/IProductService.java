package com.cg.service.product;

import com.cg.domain.dto.product.ProductCreReqDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.product.ProductUpReqDTO;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.Unit;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IProductService extends IGeneralService<Product,Long> {
    List<ProductDTO> findAllProductDTO();

    Product createProduct(ProductCreReqDTO productCreReqDTO, Category category, Unit unit);
    Optional<Product> findByIdAndDeletedFalse(Long id);

    Product update(Long productId, ProductUpReqDTO productUpReqDTO, Category category,Unit unit);

    void deleteByIdTrue(Product product);
}
