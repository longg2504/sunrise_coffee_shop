package com.cg.service.staff;

import com.cg.domain.dto.locationRegion.LocationRegionUpReqDTO;
import com.cg.domain.dto.staff.StaffCreReqDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.staff.StaffUpReqDTO;
import com.cg.domain.entity.*;
import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.repository.avatar.AvatarRepository;
import com.cg.repository.locationRegion.LocationRegionRepository;
import com.cg.repository.staff.StaffRepository;
import com.cg.service.role.IRoleService;
import com.cg.service.upload.IUploadService;
import com.cg.service.user.IUserService;
import com.cg.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private IStaffService staffService;
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
    public Optional<Staff> findByUser(User user) {
        return staffRepository.findByUser(user);
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

    @Override
    public Staff create(StaffCreReqDTO staffCreReqDTO) {
        MultipartFile file = staffCreReqDTO.getStaffAvatar();

        LocationRegion locationRegion = staffCreReqDTO.toLocationRegion();
        locationRegionRepository.save(locationRegion);

        Avatar staffAvatar = new Avatar();
        avatarRepository.save(staffAvatar);

        uploadAndSaveStaffImage(staffAvatar, file);

        Boolean exitsByUserName = userService.existsByUsername(staffCreReqDTO.getUsername());
        if(exitsByUserName) {
            throw new EmailExistsException("username đã tồn tại");
        }
        Optional<Role> optRole = roleService.findById(staffCreReqDTO.getRoleId());
        if (!optRole.isPresent()) {
            throw new DataInputException("Role không tồn tại");
        }
        try{
            User user = userService.save(staffCreReqDTO.toUser(optRole.get()));
            Staff staff = staffCreReqDTO.toStaff();
            staff.setLocationRegion(locationRegion);
            staff.setStaffAvatar(staffAvatar);
            staff.setUser(user);
            staffRepository.save(staff);

            return staff;
        }catch (DataIntegrityViolationException e) {
            throw new DataInputException("Thông tin tài khoản không hợp lệ, vui lòng kiểm tra lại thông tin");
        }
    }

    @Override
    public Staff update(StaffUpReqDTO staffUpReqDTO, Long staffId) {
        MultipartFile file = staffUpReqDTO.getStaffAvatar();
        Optional<Staff> staffOptional = staffService.findById(staffId);
        Long locationRegionId = staffOptional.get().getLocationRegion().getId();
        LocationRegionUpReqDTO locationRegionUpReqDTO = staffUpReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionUpReqDTO.toLocationRegionUp(locationRegionId);
        locationRegionRepository.save(locationRegion);

        Avatar staffAvatar = new Avatar();
        avatarRepository.save(staffAvatar);
        uploadAndSaveStaffImage(staffAvatar,file);
        Staff staffUpdate = staffUpReqDTO.toStaffChangeImage();
        staffUpdate.setId(staffId);
        staffUpdate.setStaffAvatar(staffAvatar);
        staffUpdate.setLocationRegion(locationRegion);
        staffUpdate.setUser(staffOptional.get().getUser());
        staffRepository.save(staffUpdate);
        return staffUpdate;
    }

    @Override
    public void deleteByIdTrue(Staff staff) {
        staff.setDeleted(true);
        staffRepository.save(staff);
    }

    private void uploadAndSaveStaffImage(Avatar staffAvatar, MultipartFile file) {
        try {
            Map uploadResult = uploadService.uploadImage(file, uploadUtils.buildImageUploadParamsStaff(staffAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            staffAvatar.setFileName(staffAvatar.getId() + "." + fileFormat);
            staffAvatar.setFileUrl(fileUrl);
            staffAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER_1);
            staffAvatar.setCloudId(staffAvatar.getFileFolder() + "/" + staffAvatar.getId());
            avatarRepository.save(staffAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }
}
