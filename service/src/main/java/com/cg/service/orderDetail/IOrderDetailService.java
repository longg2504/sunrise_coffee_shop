package com.cg.service.orderDetail;

import com.cg.domain.dto.orderDetail.OrderDetailByTableResDTO;
import com.cg.domain.entity.OrderDetail;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailService extends IGeneralService<OrderDetail,Long> {
    List<OrderDetailByTableResDTO> getOrderDetailByTableResDTO(Long orderId);

    Optional<OrderDetail> findByOrderDetailByIdProductAndIdOrder(Long idProduct, Long idOrder, String note, String status);

    OrderDetail findByOrderId(Long orderId);
}
