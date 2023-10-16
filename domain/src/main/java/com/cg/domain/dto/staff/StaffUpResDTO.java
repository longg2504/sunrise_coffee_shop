package com.cg.domain.dto.staff;

import com.cg.domain.dto.locationRegion.LocationRegionUpReqDTO;
import com.cg.domain.dto.locationRegion.LocationRegionUpResDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class StaffUpResDTO {
    private Long id;
    private String fullName;
    private Date dob;
    private String phone;
    private LocationRegionUpResDTO locationRegion;
    private Avatar staffAvatar;
    private User user;
}
