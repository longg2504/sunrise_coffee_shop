package com.cg.repository.product;

import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT NEW com.cg.domain.dto.product.ProductDTO " +
            "(pro.id," +
            "pro.title," +
            "pro.price," +
            "pro.unit," +
            "pro.category," +
            "pro.productAvatar) " +
            "FROM Product as pro " +
            "WHERE pro.deleted=false ")
    List<ProductDTO> findAllProductDTO();

    Optional<Product> findByIdAndDeletedFalse(Long id);

    @Query("SELECT NEW com.cg.domain.dto.product.ProductDTO" +
            "(pro.id," +
            "pro.title," +
            "pro.price," +
            "pro.unit," +
            "pro.category," +
            "pro.productAvatar) " +
            "FROM Product as pro " +
            "WHERE pro.title like :keySearch " +
            "AND pro.deleted=false ")
    List<ProductDTO> findProductByName(String keySearch);

}
