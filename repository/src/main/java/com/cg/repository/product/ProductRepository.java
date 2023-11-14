package com.cg.repository.product;

import com.cg.domain.dto.product.ProductCountDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.entity.Category;
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


//    @Query(value = "CALL sp_top10_product_best_seller", nativeQuery = true)
//    List<ProductBestSeller> getTop10ProductBestSeller();


    @Query("SELECT NEW com.cg.domain.dto.product.ProductCountDTO (" +
            "count(p.id)" +
            ") " +
            "FROM Product AS p " +
            "WHERE p.deleted = false "
    )
    ProductCountDTO countProduct();


    @Query("SELECT NEW com.cg.domain.dto.product.ProductDTO (" +
            "pr.id, " +
            "pr.title, " +
            "pr.price, " +
            "pr.unit, " +
            "pr.category, " +
            "pr.productAvatar " +
            ") " +
            "From Product AS pr " +
            "WHERE (:category IS NULL OR pr.category = :category) " +
            "AND pr.title LIKE %:search% " +
            "AND pr.deleted = false ")
    Page<ProductDTO> findAllByCategoryAndSearch(@Param("category") Category category, @Param("search") String search, Pageable pageable);

}
