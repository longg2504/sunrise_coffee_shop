package com.cg.domain.dto.unit;

import com.cg.domain.entity.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UnitDTO {
    private Long id;
    private String title;

    public Unit toUnit() {
        return new Unit()
                .setId(id)
                .setTitle(title);
    }
}
