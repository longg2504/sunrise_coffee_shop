package com.cg.controller;

import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IUserService userService;

    @GetMapping()
    public String showDashboardPage(){
        return "dashboard/dashboard";
    }
}
