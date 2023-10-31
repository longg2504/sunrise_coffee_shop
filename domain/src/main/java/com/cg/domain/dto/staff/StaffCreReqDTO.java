package com.cg.domain.dto.staff;

import com.cg.domain.dto.locationRegion.LocationRegionDTO;
import com.cg.domain.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class StaffCreReqDTO implements Validator {
    private String fullName;
    private Date dob;
    private String phone;
    private String provinceId;
    private String provinceName;
    private String districtId;
    private String districtName;
    private String wardId;
    private String wardName;
    private String address;
    private MultipartFile staffAvatar;
    private String username;
    private Long roleId;

    public LocationRegion toLocationRegion() {
        return new LocationRegion()
                .setProvinceId(provinceId)
                .setProvinceName(provinceName)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address)
                ;
    }

    public Staff toStaff() {
        return new Staff()
                .setId(null)
                .setFullName(fullName)
                .setDob(dob)
                .setPhone(phone)
                ;
    }

    public User toUser(Role role) {
        return new User()
                .setUsername(username)
                .setPassword("123123")
                .setRole(role)
                ;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return StaffCreReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StaffCreReqDTO staffCreReqDTO = (StaffCreReqDTO) target;
        String fullName = staffCreReqDTO.fullName;
        String phone = staffCreReqDTO.phone;
        String username = staffCreReqDTO.username;
        if (fullName.isEmpty()) {
            errors.rejectValue("fullName", "fullName.null", "Tên không được phép rỗng");
            return;
        }
        if (phone.isEmpty()) {
            errors.rejectValue("phone", "phone.null", "Số điện thoại không được phép rỗng");
        }
        if (username.isEmpty()) {
            errors.rejectValue("username", "username.null", "Tên tài khoản không được phép rỗng");

        }
    }
}
