package com.cg.service.staff;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.User;
import com.cg.repository.avatar.AvatarRepository;
import com.cg.repository.locationRegion.LocationRegionRepository;
import com.cg.repository.staff.StaffRepository;
import com.cg.service.role.IRoleService;
import com.cg.service.upload.IUploadService;
import com.cg.service.user.IUserService;
import com.cg.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class StaffServiceImpl implements IStaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private IUploadService uploadService;
    @Autowired
    private UploadUtils uploadUtils;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private LocationRegionRepository locationRegionRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @Override
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public void delete(Staff staff) {
        staffRepository.delete(staff);
    }

    @Override
    public void deleteById(Long id) {
        staffRepository.deleteById(id);
    }

    @Override
    public Page<StaffDTO> findAllStaffDTOPage(Pageable pageable) {
        return staffRepository.findAllStaffDTOPage(pageable);
    }

    @Override
    public Page<StaffDTO> findStaffByKeySearch(String keySearch, Pageable pageable) {
        return staffRepository.findStaffByKeySearch(keySearch,pageable);
    }

    @Override
    public Optional<Staff> findByUserId(Long userId) {
        return staffRepository.findByUserId(userId);
    }

    @Override
    public Optional<Staff> findByUserAndDeletedIsFalse(User user) {
        return staffRepository.findByUserAndDeletedIsFalse(user);
    }

    @Override
    public Optional<Staff> findByIdAndDeletedFalse(Long id) {
        return staffRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<StaffDTO> findAllStaffDTO() {
        return staffRepository.findAllStaffDTO();
    }
}
