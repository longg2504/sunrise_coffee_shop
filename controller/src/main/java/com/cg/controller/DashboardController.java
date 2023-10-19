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
        return "/dashboard/dashboard";
    }

    @GetMapping("/table")
    public String showTablePage() {
        return "/dashboard/list-table";
    }

    @GetMapping("/staff")
    public String showStaffPage() {
        return "/dashboard/list-staff";
    }

    @GetMapping("/bill")
    public String showBillPage() {
        return "/dashboard/list-bill";
    }

    @GetMapping("/product")
    public String showProductPage() {
        return "/dashboard/list-product";
    }

}
