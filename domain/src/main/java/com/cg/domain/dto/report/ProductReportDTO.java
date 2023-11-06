package com.cg.domain.dto.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
public class ProductReportDTO {
    private Long id;

    private String title;

    private String fileFolder;

    private String fileName;

    private Long quantity;

    private BigDecimal amount;


    public ProductReportDTO(Long id, String title, String fileFolder, String fileName, Long quantity, BigDecimal amount) {
        this.id = id;
        this.title = title;
        this.fileFolder = fileFolder;
        this.fileName = fileName;
        this.quantity = quantity;
        this.amount = amount;
    }
}
