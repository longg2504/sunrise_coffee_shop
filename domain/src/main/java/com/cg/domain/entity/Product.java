package com.cg.domain.entity;

import com.cg.domain.dto.product.ProductCreResDTO;
import com.cg.domain.dto.product.ProductDTO;
import com.cg.domain.dto.product.ProductUpReqDTO;
import com.cg.domain.dto.product.ProductUpResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="products")
@Accessors(chain = true)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(precision = 10, scale = 0, nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name="unit_id" , referencedColumnName = "id" , nullable = false)
    private Unit unit;

    @ManyToOne
    @JoinColumn(name="category_id" , referencedColumnName = "id" , nullable = false)
    private Category category;

    @OneToOne
    @JoinColumn(name = "product_avatar_id",referencedColumnName = "id" ,  nullable = false)
    private Avatar productAvatar;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    public ProductCreResDTO toProductCreResDTO() {
        return new ProductCreResDTO()
                .setId(id)
                .setTitle(title)
                .setPrice(price)
                .setUnit(unit)
                .setCategory(category)
                .setProductAvatar(productAvatar.toAvatarResDTO());
    }

    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setTitle(title)
                .setPrice(price)
                .setUnit(unit.toUnitDTO())
                .setCategory(category.toCategoryDTO())
                .setProductAvatar(productAvatar.toAvatarResDTO());
    }

    public ProductUpResDTO toProductUpResDTO() {
        return new ProductUpResDTO()
                .setId(id)
                .setTitle(title)
                .setPrice(price)
                .setUnit(unit)
                .setCategory(category)
                .setProductAvatar(productAvatar.toAvatarResDTO());
    }

}
