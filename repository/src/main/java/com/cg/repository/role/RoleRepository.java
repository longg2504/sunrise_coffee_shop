package com.cg.repository.role;

import com.cg.domain.dto.role.RoleDTO;
import com.cg.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

}
