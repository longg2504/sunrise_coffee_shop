package com.cg.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductCountDTO {
    private Long countProduct;

    public ProductCountDTO(Long countProduct) {
        this.countProduct = countProduct;
    }
}
