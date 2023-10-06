package com.cg.domain.dto.role;

import com.cg.domain.entity.Role;
import com.cg.domain.enums.ERole;
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
public class RoleDTO {
    private Long id;

    private String code;

    private ERole name;

    public Role toRole() {
        return new Role()
                .setId(id)
                .setCode(code)
                .setName(name)
                ;
    }
}
