package com.cg.domain.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class BillOfTheDayDTO {
    private Long count;

    public BillOfTheDayDTO(Long count) {
        this.count = count;
    }
}
