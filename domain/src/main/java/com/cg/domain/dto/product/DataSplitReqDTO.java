package com.cg.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataSplitReqDTO {
    private String sourceTableId;
    private String targetTableId;

    private List<ProductSplitReqDTO> products;
}
