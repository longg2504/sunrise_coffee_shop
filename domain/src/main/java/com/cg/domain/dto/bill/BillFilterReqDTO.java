package com.cg.domain.dto.bill;

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
public class BillFilterReqDTO {
        private Integer currentPageNumber;
        @JsonFormat(pattern="yyyy-MM-dd")
        private Date billFrom;
        @JsonFormat(pattern="yyyy-MM-dd")
        private Date billTo;
        private Boolean paid;

}
