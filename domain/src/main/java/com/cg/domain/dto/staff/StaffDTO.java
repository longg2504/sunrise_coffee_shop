package com.cg.domain.dto.staff;

import com.cg.domain.dto.avatar.AvatarDTO;
import com.cg.domain.dto.locationRegion.LocationRegionDTO;
import com.cg.domain.entity.Avatar;
import com.cg.domain.entity.LocationRegion;
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
public class StaffDTO {
    private Long id;
    private String fullName;
    private Date dob;
    private String phone;
    private LocationRegionDTO locationRegion;
    private Avatar staffAvatar;
    private User user;

    public StaffDTO(Long id, String fullName, Date dob, String phone, LocationRegion locationRegion, Avatar staffAvatar, User user) {
        this.id = id;
        this.fullName = fullName;
        this.dob = dob;
        this.phone = phone;
        this.locationRegion = locationRegion.toLocationRegionDTO();
        this.staffAvatar = staffAvatar;
        this.user = user;
    }
}
