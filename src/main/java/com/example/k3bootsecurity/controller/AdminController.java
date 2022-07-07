package com.example.k3bootsecurity.controller;

import com.example.k3bootsecurity.entity.Role;
import com.example.k3bootsecurity.entity.UserEntity;
import com.example.k3bootsecurity.model.User;
import com.example.k3bootsecurity.service.AppService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final AppService<UserEntity> entityAppService;
    private final AppService<Role> roleAppService;

    public AdminController(AppService<UserEntity> service, AppService<Role> roleAppService, PasswordEncoder passwordEncoder) {
        this.entityAppService = service;
        this.roleAppService = roleAppService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "")
    public String allUsers(ModelMap model) {
        List<UserEntity> entities = entityAppService.getAll();
        List<User> users = entities.stream().map(UserEntity::toUser).collect(Collectors.toList());
        model.addAttribute("users", users);
        return "listUsers";
    }

    @PostMapping(path = "/updateUser")
    public String updateUser(@RequestParam Long id, Model model) {
        User updateUser = entityAppService.getById(id).toUser();
        model.addAttribute("user", updateUser);
        return "updateUser";
    }

    @PutMapping(path = "/update")
    public String update(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/updateUser";
        } else {
            UserEntity userEntity = entityAppService.getById(user.getId());
            userEntity.setName(user.getName());
            userEntity.setSurname(user.getSurname());
            userEntity.setAge(user.getAge());
            userEntity.setEmail(user.getEmail());
            entityAppService.saveOrUpdate(userEntity);
            return "redirect:/admin";
        }
    }

    @DeleteMapping(path = "/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        UserEntity entity = entityAppService.getById(id);
        entityAppService.remove(entity);
        return "redirect:/admin";
    }

    @GetMapping(path = "/createUser")
    public String createUser(ModelMap model) {
        UserEntity userEntity = new UserEntity();
        List<Role> roleList = roleAppService.getAll();
        model.addAttribute("user", userEntity);
        model.addAttribute("roleList", roleList);
        return "createUser";
    }

    @PostMapping(path = "/create")
    public String create(@Valid @ModelAttribute("user") UserEntity userEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/createUser";
        } else {
            String password = passwordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(password);
            entityAppService.saveOrUpdate(userEntity);
            return "redirect:/admin";
        }
    }

}
