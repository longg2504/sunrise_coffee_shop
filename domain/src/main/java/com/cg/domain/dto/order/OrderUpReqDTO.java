package com.cg.domain.dto.order;

import com.cg.domain.enums.EOrderDetailStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderUpReqDTO {
    private Long tableId;
    private Long quantity;
    private Long productId;
    private String note;
    private EOrderDetailStatus status;
}
