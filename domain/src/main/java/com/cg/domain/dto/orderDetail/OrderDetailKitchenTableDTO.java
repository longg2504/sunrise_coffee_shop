package com.cg.domain.dto.orderDetail;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderDetailKitchenTableDTO {
    private Long orderDetailId;
    private String tableName;
    private Long productId;
    private String productTilte;
    private String note;
    private Long quantity;
    private String unitTilte;

    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updateAt;
}
