package com.cg.service.user;


import com.cg.domain.entity.User;
import com.cg.service.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IUserService extends IGeneralService<User, Long>, UserDetailsService {

    Boolean existsByUsername(String username);

    User getByUsername(String username);

    Optional<User> findByName(String userName);


}
