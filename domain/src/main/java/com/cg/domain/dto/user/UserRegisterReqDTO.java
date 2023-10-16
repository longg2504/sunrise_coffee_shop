package com.cg.domain.dto.user;

import com.cg.domain.entity.Role;
import com.cg.domain.entity.User;
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
public class UserRegisterReqDTO {
    private String username;
    private String password;
    private Long roleId;

    public User toUser(Role role){
        return new User()
                .setUsername(username)
                .setPassword(password)
                .setRole(role)
                ;
    }
}
