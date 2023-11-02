package com.cg.domain.dto.report;

import java.math.BigDecimal;

public interface IYearReportDTO {
    int getMonth();
    BigDecimal getTotalAmount();
    Long getCount();
}
