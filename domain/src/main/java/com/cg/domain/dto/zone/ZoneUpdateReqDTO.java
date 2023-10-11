package com.cg.domain.dto.zone;

import com.cg.domain.entity.Zone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class ZoneUpdateReqDTO implements Validator {
    private String title;

    @Override
    public boolean supports(Class<?> clazz) {
        return ZoneCreateReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ZoneUpdateReqDTO zoneUpdateReqDTO = (ZoneUpdateReqDTO) target;

        String title = zoneUpdateReqDTO.title;
        if (title.isEmpty()) {
            errors.rejectValue("title","title.null","Tên không được phép rỗng");

        }
    }

    public Zone toZoneUpdateReqDTO(Long zoneId) {
        return new Zone()
                .setId(zoneId)
                .setTitle(title);
    }
}
