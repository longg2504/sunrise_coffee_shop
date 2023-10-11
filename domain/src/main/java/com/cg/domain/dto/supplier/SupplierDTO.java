package com.cg.domain.dto.supplier;

import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class SupplierDTO {
    private Long id;
    private String title;
    private String address;
    private String phone;
    private String status;



}
