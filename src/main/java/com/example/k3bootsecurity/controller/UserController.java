package com.example.k3bootsecurity.controller;

import com.example.k3bootsecurity.entity.UserEntity;
import com.example.k3bootsecurity.model.User;
import com.example.k3bootsecurity.service.AppService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private AppService<UserEntity> service;

    public UserController(AppService<UserEntity> service) {
        this.service = service;
    }

    @GetMapping(path = "")
    public String userPage(Principal principal, Model model) {
        User user = service.getByName(principal.getName()).toUser();
        model.addAttribute("user", user);
        return "userPage";
    }
}
