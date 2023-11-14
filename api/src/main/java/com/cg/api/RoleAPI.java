package com.cg.api;

import com.cg.domain.dto.role.RoleDTO;
import com.cg.domain.entity.Role;
import com.cg.service.role.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/roles")
public class RoleAPI {
    @Autowired
    private IRoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRole(){
        List<Role>  roleDTOS = roleService.findAll();
        if(roleDTOS.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<Role> result = new ArrayList<>();
        for(Role item : roleDTOS){
            if(!item.getCode().equals("ADMIN")){
                result.add(item);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
