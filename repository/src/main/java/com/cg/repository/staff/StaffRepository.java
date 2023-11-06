package com.cg.repository.staff;

import com.cg.domain.dto.staff.StaffCountDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {

    @Query("SELECT NEW com.cg.domain.dto.staff.StaffDTO" +
            "(" +
            "st.id," +
            "st.fullName," +
            "st.dob," +
            "st.phone," +
            "st.locationRegion," +
            "st.staffAvatar," +
            "st.user" +
            ") " +
            "FROM Staff as st " +
            "WHERE st.deleted =false " +
            "ORDER BY st.id ASC"
    )
    Page<StaffDTO> findAllStaffDTOPage(Pageable pageable);
    @Query("SELECT NEW com.cg.domain.dto.staff.StaffDTO" +
            "(" +
            "st.id," +
            "st.fullName," +
            "st.dob," +
            "st.phone," +
            "st.locationRegion," +
            "st.staffAvatar," +
            "st.user" +
            ")" +
            "FROM Staff as st " +
            "WHERE st.deleted = false " +
            "ORDER BY st.id ASC"
    )
    List<StaffDTO> findAllStaffDTO();
    @Query("SELECT NEW com.cg.domain.dto.staff.StaffDTO" +
            "(" +
            "st.id," +
            "st.fullName," +
            "st.dob," +
            "st.phone," +
            "st.locationRegion," +
            "st.staffAvatar," +
            "st.user" +
            ") " +
            "FROM Staff as st " +
            "WHERE (st.fullName like :keySearch " +
            "OR st.locationRegion.provinceName like :keySearch " +
            "OR st.user.role.code like :keySearch)" +
            "AND st.deleted = false "
    )
    Page<StaffDTO> findStaffByKeySearch(@Param("keySearch") String keySearch, Pageable pageable);

    Optional<Staff> findByUserId(Long userId);

    Optional<Staff> findByUserAndDeletedIsFalse(User user);

    Optional<Staff> findByIdAndDeletedFalse(Long id);

    Optional<Staff> findByUser(User user);

    @Query("SELECT " +
            "sf " +
            "FROM Staff AS sf " +
            "JOIN User AS us " +
            "ON sf.user = us " +
            "AND us.username = :username"
    )
    Optional<Staff> findByUsername(@Param("username") String username);

    @Query("SELECT NEW com.cg.domain.dto.staff.StaffCountDTO (" +
            "count(st.id) " +
            ") " +
            "FROM Staff AS st " +
            "WHERE st.deleted = false "
    )
    StaffCountDTO countStaff ();


}
