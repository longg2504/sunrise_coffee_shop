package com.cg.service.staff;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.User;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IStaffService extends IGeneralService<Staff,Long> {
    Page<StaffDTO> findAllStaffDTOPage(Pageable pageable);
    List<StaffDTO> findStaffByKeySearch(String keySearch);

    Optional<Staff> findByUserId(Long userId);

    Optional<Staff> findByUserAndDeletedIsFalse(User user);

    Optional<Staff> findByIdAndDeletedFalse(Long id);

    List<StaffDTO> findAllStaffDTO();
}
