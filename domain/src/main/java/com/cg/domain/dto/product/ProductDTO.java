package com.cg.domain.dto.product;

import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.dto.unit.UnitDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.domain.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ProductDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private UnitDTO unit;
    private CategoryDTO category;
    private AvatarResDTO productAvatar;

    public ProductDTO(Long id, String title, BigDecimal price, Unit unit, Category category, Avatar avatar) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.unit = unit.toUnitDTO();
        this.category = category.toCategoryDTO();
        this.productAvatar = avatar.toAvatarResDTO();
    }
    public Product toProduct() {
        return new Product()
                .setId(id)
                .setTitle(title)
                .setPrice(price)
                .setCategory(category.toCategory())
                .setProductAvatar(productAvatar.toAvatar());
    }
}
