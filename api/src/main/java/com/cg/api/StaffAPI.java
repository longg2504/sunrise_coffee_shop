package com.cg.api;

import com.cg.domain.dto.locationRegion.LocationRegionUpReqDTO;
import com.cg.domain.dto.staff.StaffCreReqDTO;
import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.dto.staff.StaffUpReqDTO;
import com.cg.domain.dto.staff.StaffUpResDTO;
import com.cg.domain.entity.LocationRegion;
import com.cg.domain.entity.Role;
import com.cg.domain.entity.Staff;
import com.cg.domain.entity.User;
import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.service.locationRegion.ILocationRegionService;
import com.cg.service.role.IRoleService;
import com.cg.service.staff.IStaffService;
import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
public class StaffAPI {
    @Autowired
    private IStaffService staffService;
    @Autowired
    private ValidateUtils validateUtils;
    @Autowired
    private AppUtils appUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private ILocationRegionService locationRegionService;
    @Autowired
    private IRoleService roleService;


//    @GetMapping
//    public ResponseEntity<?>  getAllStaff() {
//        List<StaffDTO> staffDTOS = staffService.findAllStaffDTO();
//        if(staffDTOS.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(staffDTOS, HttpStatus.OK);
//    }

    @GetMapping("/{staffId}")
    public ResponseEntity<?> getStaffById(@PathVariable Long staffId){
        Optional<Staff> optionalStaff = staffService.findByIdAndDeletedFalse(staffId);
        if(!optionalStaff.isPresent()) {
            throw new DataInputException("ID nhân viên không hợp lệ");
        }
        Staff staff = optionalStaff.get();
        StaffDTO staffDTO = staff.toStaffDTO();
        staffDTO.setStaffAvatar(staff.getStaffAvatar());
        return new ResponseEntity<>(staffDTO, HttpStatus.OK);
    }

//    @GetMapping("/page")
//    public ResponseEntity<Page<StaffDTO>> getAllStaffDTOByPage(Pageable pageable){
//        try{
//            Page<StaffDTO> staffDTOS = staffService.findAllStaffDTOPage(pageable);
//
//            if(staffDTOS.isEmpty()){
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//            return new ResponseEntity<>(staffDTOS,HttpStatus.NO_CONTENT);
//        }catch (Exception e){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping
    public ResponseEntity<?> getStaffBySearch(@RequestParam(defaultValue = "")String search, Pageable pageable) {
        search =  '%' + search + '%';
        Page<StaffDTO> staffDTOS = staffService.findStaffByKeySearch(search, pageable);
        if(staffDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(staffDTOS, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStaff(@ModelAttribute StaffCreReqDTO staffCreReqDTO, BindingResult bindingResult){
        new StaffCreReqDTO().validate(staffCreReqDTO, bindingResult);
        if(bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitsByUsername = userService.existsByUsername(staffCreReqDTO.getUsername());

        if(exitsByUsername) {
            throw new EmailExistsException("Tài khoản đã tồn tại vui lòng xem lại thông tin!!!");
        }

        Optional<Role> roleOptional = roleService.findById(staffCreReqDTO.getRoleId());
        if(!roleOptional.isPresent()){
            throw new DataInputException("Role này không tôn tại vui lòng xem lại!!!");
        }

        Staff staff = staffService.create(staffCreReqDTO);
        StaffDTO staffDTO = staff.toStaffDTO();
        staffDTO.setStaffAvatar(staff.getStaffAvatar());
        return new ResponseEntity<>(staffDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{staffId}")
    public ResponseEntity<?> updateStaff(@PathVariable("staffId") String staffIdStr, StaffUpReqDTO staffUpReqDTO, BindingResult bindingResult){
        if(!validateUtils.isNumberValid(staffIdStr)){
            throw new DataInputException("Mã nhân viên không hợp lệ");
        }
        Long staffId = Long.parseLong(staffIdStr);
        Optional<Staff> staffOptional = staffService.findByIdAndDeletedFalse(staffId);

        if(!staffOptional.isPresent()) {
            throw new DataInputException("Mã nhân viên không tồn tại");
        }

        User user = userService.findById(staffOptional.get().getUser().getId()).orElseThrow(() ->{
            throw new DataInputException("Tài khoản, mật khẩu không đúng");
        });
        new StaffUpReqDTO().validate(staffUpReqDTO,bindingResult);
        if(bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if(staffUpReqDTO.getStaffAvatar() == null){
            Staff staff = staffUpReqDTO.toStaffUpReqDTO(staffId);

            Long locationRegionId = staffOptional.get().getLocationRegion().getId();
            LocationRegionUpReqDTO locationRegionUpReqDTO = staffUpReqDTO.getLocationRegion();
            LocationRegion locationRegion = locationRegionUpReqDTO.toLocationRegionUp(locationRegionId);
            locationRegionService.save(locationRegion);

            staff.setStaffAvatar(staffOptional.get().getStaffAvatar());
            staff.setLocationRegion(locationRegion);
            staff.setUser(user);
            staffService.save(staff);

            StaffUpResDTO staffUpResDTO = staff.toStaffUpResDTO();
            staffUpResDTO.setStaffAvatar(staff.getStaffAvatar());
            staffUpResDTO.setUser(staff.getUser());
            return new ResponseEntity<>(staffUpResDTO, HttpStatus.OK);
        }
        else{
            Staff staff = staffService.update(staffUpReqDTO, staffId);
            StaffUpResDTO staffUpResDTO = staff.toStaffUpResDTO();
            staffUpResDTO.setStaffAvatar(staff.getStaffAvatar());
            staffUpResDTO.setUser(staff.getUser());
            return new ResponseEntity<>(staffUpResDTO,HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<?> deleteStaff(@PathVariable("staffId") String staffIdStr) {
        if (!validateUtils.isNumberValid(staffIdStr)) {
            throw new DataInputException("UserId không hợp lệ");
        }

        Long staffId = Long.valueOf(staffIdStr);

        Staff staff = staffService.findById(staffId).orElseThrow(() -> {
            throw new DataInputException("UserId không tồn tại");
        });

        staffService.deleteByIdTrue(staff);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
