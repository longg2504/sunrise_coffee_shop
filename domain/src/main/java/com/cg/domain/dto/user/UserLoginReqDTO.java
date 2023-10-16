package com.cg.domain.dto.user;

import com.cg.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UserLoginReqDTO implements Validator {


    @NotBlank(message = "Vui lòng nhập tài khoản!")
    @Pattern(regexp = "^[\\w]+@([\\w-]+\\.)+[\\w-]{2,6}$", message = "Email không hợp lệ!")
    @Size(min = 8, max = 35, message = "Độ dài email nằm trong khoảng 8-35 ký tự!")
    private String username;

    @Size(min = 3, max = 50, message = "Độ dài mật khẩu nằm trong khoảng 3-50 ký tự!")
    @NotBlank(message = "Vui lòng nhập mật khẩu!")
    private String password;


    public User toUser() {
        return new User()
                .setUsername(username)
                .setPassword(password)
                ;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserLoginReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserLoginReqDTO userLoginReqDTO = (UserLoginReqDTO) target;

        String username = userLoginReqDTO.username;
        String password = userLoginReqDTO.password;

        if(username.isEmpty()) {
            errors.rejectValue("username", "username.null", "Tài khoản không được để trống!!!");
        }
        if(password.isEmpty()) {
            errors.rejectValue("password", "password.null", "Mật khẩu không được để trống!!!");
        }
    }
}