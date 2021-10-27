package com.example.belolipetsckiy.belolipetsckiy_boot.controllers;

import com.example.belolipetsckiy.belolipetsckiy_boot.service.RoleService;
import com.example.belolipetsckiy.belolipetsckiy_boot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String index () {
        return "user/index1";
    }

}
