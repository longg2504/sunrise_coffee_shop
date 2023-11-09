package com.cg.controller;

import com.cg.domain.entity.Role;
import com.cg.domain.entity.User;
import com.cg.exception.DataInputException;
import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IUserService userService;

    @GetMapping()
    public ModelAndView showDashboardPage(){
        String userName = appUtils.getPrincipalUsername();
        ModelAndView modelAndView = new ModelAndView();
        userName = userName.substring(0, userName.indexOf("@")).toUpperCase();
        modelAndView.addObject("userName",userName);
        modelAndView.setViewName("/dashboard/dashboard");
        return modelAndView;
    }

    @GetMapping("/table")
    public String showTablePage(Model model) {
        String username = appUtils.getPrincipalUsername();

        Optional<User> userOptional = userService.findByName(username);

        if (!userOptional.isPresent()) {
            throw new DataInputException("User not valid");
        }

        Role role = userOptional.get().getRole();
        String roleCode = role.getCode();

        username = username.substring(0, username.indexOf("@")).toUpperCase();
        model.addAttribute("userName", username);
        model.addAttribute("roleCode", roleCode);
        return "/dashboard/list-table";
    }

    @GetMapping("/staff")
    public String showStaffPage(Model model) {
        String username = appUtils.getPrincipalUsername();

        Optional<User> userOptional = userService.findByName(username);

        if (!userOptional.isPresent()) {
            throw new DataInputException("User not valid");
        }

        Role role = userOptional.get().getRole();
        String roleCode = role.getCode();

        username = username.substring(0, username.indexOf("@")).toUpperCase();
        model.addAttribute("userName", username);
        model.addAttribute("roleCode", roleCode);
        return "/dashboard/list-staff";
    }

    @GetMapping("/bill")
    public String showBillPage(Model model) {
        String username = appUtils.getPrincipalUsername();

        Optional<User> userOptional = userService.findByName(username);

        if (!userOptional.isPresent()) {
            throw new DataInputException("User not valid");
        }

        Role role = userOptional.get().getRole();
        String roleCode = role.getCode();

        username = username.substring(0, username.indexOf("@")).toUpperCase();
        model.addAttribute("userName", username);
        model.addAttribute("roleCode", roleCode);
        return "/dashboard/list-bill";
    }

    @GetMapping("/product")
    public String showProductPage(Model model) {
        String username = appUtils.getPrincipalUsername();

        Optional<User> userOptional = userService.findByName(username);

        if (!userOptional.isPresent()) {
            throw new DataInputException("User not valid");
        }

        Role role = userOptional.get().getRole();
        String roleCode = role.getCode();

        username = username.substring(0, username.indexOf("@")).toUpperCase();
        model.addAttribute("userName", username);
        model.addAttribute("roleCode", roleCode);
        return "/dashboard/list-product";
    }

}
