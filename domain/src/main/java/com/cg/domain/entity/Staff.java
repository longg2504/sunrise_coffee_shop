package com.cg.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
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

    @Column(nullable = false , length = 10)
    private String phone;

    @ManyToOne(cascade = CascadeType.PERSIST)
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
}
