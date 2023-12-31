package com.cg.domain.entity;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.staff.StaffUpResDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="staffs")
@Accessors(chain = true)
public class Staff extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name" , nullable = false)
    private String fullName;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dob;

    @Column(nullable = false , length = 10)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "location_region_id", referencedColumnName = "id", nullable = false)
    private LocationRegion locationRegion;

    @OneToOne
    @JoinColumn(name = "staff_avatar_id",referencedColumnName = "id",  nullable = false)
    private Avatar staffAvatar;

    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<Order> orders;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "staff")
    @JsonIgnore
    private List<Bill> bills;

    public StaffDTO toStaffDTO() {
        return new StaffDTO()
                .setId(id)
                .setFullName(fullName)
                .setDob(dob)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegionDTO())
                .setStaffAvatar(staffAvatar)
                .setUser(user)
                ;
    }

    public StaffUpResDTO toStaffUpResDTO() {
        return new StaffUpResDTO()
                .setId(id)
                .setFullName(fullName)
                .setDob(dob)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegionUpResDTO())
                .setStaffAvatar(staffAvatar)
                ;
    }
}
