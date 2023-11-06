package com.cg.domain.dto.report;

import com.cg.domain.dto.product.IProductReportDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AllReportDTO {
    IReportDTO dayReport;
    IReportDTO monthReport;
    List<IYearReportDTO> yearReport;
    List<IProductReportDTO> top5BestSellReport;
    List<IDayToDayReportDTO> tenDaysAgoReport;
    List<I6MonthAgoReportDTO> sixMonthAgoReport;
    Long countStaff;
    Long countTable;
    Long countProduct;

}
