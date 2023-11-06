package com.cg.domain.dto.product;

import java.math.BigDecimal;

public interface IProductReportDTO{
        Long getId();

        String getTitle();

        String getFileFolder();

        String getFileName();

        Long getQuantity();

        BigDecimal getAmount();

}
