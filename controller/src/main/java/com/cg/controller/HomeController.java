package com.cg.controller;


import com.cg.domain.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeController {
    @GetMapping("/login")
    public String showLoginPage() {
        return "login/login" ;
    }
}
