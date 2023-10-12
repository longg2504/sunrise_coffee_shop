package com.cg.domain.dto.zone;

import com.cg.domain.entity.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class ZoneDTO {
    private Long id;
    private String title;

    public ZoneDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Zone toZone() {
        return new Zone()
                .setId(id)
                .setTitle(title);
    }
}
