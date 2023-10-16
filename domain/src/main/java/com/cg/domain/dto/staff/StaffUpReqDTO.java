package com.cg.domain.dto.staff;

import com.cg.domain.dto.locationRegion.LocationRegionUpReqDTO;
import com.cg.domain.entity.Staff;
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
public class StaffUpReqDTO implements Validator {
    private String fullName;
    private Date dob;
    private String phone;
    private LocationRegionUpReqDTO locationRegion;
    private MultipartFile staffAvatar;


    public Staff toStaffChangeImage() {
        return new Staff()
                .setFullName(fullName)
                .setDob(dob)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion())
                ;
    }

    public Staff toStaffUpReqDTO(Long staffId){
        return new Staff()
                .setId(staffId)
                .setFullName(fullName)
                .setDob(dob)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion());

    }


    @Override
    public boolean supports(Class<?> clazz) {
        return StaffUpReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StaffUpReqDTO staffUpReqDTO = (StaffUpReqDTO) target;
        String fullName = staffUpReqDTO.fullName;
        String phone = staffUpReqDTO.phone;

        if (fullName.isEmpty()) {
            errors.rejectValue("fullName", "fullName.null", "Tên không được phép rỗng");
            return;
        }
        if (fullName.length() >= 25 || fullName.length() <= 5) {
            errors.rejectValue("fullName", "fullName.length", "Tên không ít hơn 5 kí tự và dài hơn 25 kí tự");
        }
        if (phone.isEmpty()) {
            errors.rejectValue("phone", "phone.null", "Số điện thoại không được phép rỗng");
        }
    }
}


