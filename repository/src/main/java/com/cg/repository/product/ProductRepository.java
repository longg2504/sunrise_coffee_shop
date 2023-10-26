package com.cg.repository.product;

import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            "WHERE pro.deleted=false")
    List<ProductDTO> findAllProductDTO();

    Optional<Product> findByIdAndDeletedFalse(Long id);


    Page<ProductDTO> findProductsByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT NEW com.cg.domain.dto.product.ProductDTO " +
            "(pro.id," +
            "pro.title," +
            "pro.price," +
            "pro.unit," +
            "pro.category," +
            "pro.productAvatar) " +
            "FROM Product as pro " +
            "WHERE (pro.title like :keySearch AND pro.deleted=false)")
    Page<ProductDTO> findProductByKeySearch(@Param("keySearch") String keySearch, Pageable pageable);

    List<ProductDTO> findAllByDeletedFalse(Sort sort);
    @Query("select new com.cg.domain.dto.product.ProductDTO (" +
            "p.id, " +
            "p.title, " +
            "p.price, " +
            "p.unit, " +
            "p.category, " +
            "p.productAvatar) " +
            "from Product as p " +
            "where (p.title like %:title%) and p.deleted=false ")
    Page<ProductDTO> findAllByDeletedFalse(String title,Pageable pageable);
    @Query("select new com.cg.domain.dto.product.ProductDTO (" +
            "p.id, " +
            "p.title, " +
            "p.price, " +
            "p.unit, " +
            "p.category, " +
            "p.productAvatar) " +
            "from Product as p " +
            "where (p.title like %:title%) and p.deleted=false ")
    List<ProductDTO> findAllByDeletedFalseAndTitleLike(String title);
    @Query("SELECT NEW com.cg.domain.dto.product.ProductDTO ( " +
            "pro.id, " +
            "pro.title, " +
            "pro.price, " +
            "pro.unit, " +
            "pro.category, " +
            "pro.productAvatar " +
            ") " +
            "FROM Product as pro " +
            "WHERE pro.deleted = false " +
            "ORDER BY pro.id ASC"
    )
    Page<ProductDTO> findAllProductDTOPage(Pageable pageable);
}
