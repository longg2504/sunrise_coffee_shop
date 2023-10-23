package com.cg.domain.dto.product;

import com.cg.domain.dto.avatar.AvatarResDTO;
import com.cg.domain.entity.Category;
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
public class ProductCreResDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private Unit unit;
    private Category category;
    private AvatarResDTO productAvatar;

}
