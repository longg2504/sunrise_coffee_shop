package com.cg.domain.dto.product;

import com.cg.domain.dto.avatar.AvatarResDTO;
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
    private String unitTitle;
    private String categoryTitle;
    private AvatarResDTO productAvatar;

}
