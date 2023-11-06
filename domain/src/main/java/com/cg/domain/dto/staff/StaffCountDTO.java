package com.cg.domain.dto.staff;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffCountDTO {
    private Long countStaff;

    public StaffCountDTO(Long countStaff) {
        this.countStaff = countStaff;
    }
}
