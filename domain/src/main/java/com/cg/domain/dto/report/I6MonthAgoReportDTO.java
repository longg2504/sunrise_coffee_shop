package com.cg.domain.dto.report;

import java.math.BigDecimal;

public interface I6MonthAgoReportDTO {
    int getMonth();
    Long getYear();

    BigDecimal getTotalAmount();
    Long getCount();
}


