package com.cg.domain.dto.order;

import com.cg.domain.dto.orderDetail.IOrderDetailKitchenTableDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderKitchenTableDTO {
    private Long tableId;

    private String tableName;

    @JsonFormat(pattern = "HH:mm dd-MM-yyyy", timezone="Asia/Ho_Chi_Minh")
    private Date updatedAt;

    private int countProduct;

    private Long orderId;

    private List<IOrderDetailKitchenTableDTO> orderDetails;
}
