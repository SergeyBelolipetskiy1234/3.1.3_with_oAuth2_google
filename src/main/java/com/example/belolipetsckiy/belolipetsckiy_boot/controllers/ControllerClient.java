package com.example.belolipetsckiy.belolipetsckiy_boot.controllers;

import com.example.belolipetsckiy.belolipetsckiy_boot.models.User;
import com.example.belolipetsckiy.belolipetsckiy_boot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.logging.Logger;

@Controller
public class ControllerClient {
    private UserService userService;
    private final Logger LOGGER = Logger.getLogger("<-------- LOG: ControllerClient -------->");

    public ControllerClient(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/client")
    public String client () {
        return "/client/client";
    }

    @GetMapping("/currentClient")
    @ResponseBody
    public ResponseEntity<User> currentClient(Principal principal) {
        String[] name = principal.getName().split(" ");
        LOGGER.info(name[0]);
        return new ResponseEntity<>(userService.getUserByName(name[0]), HttpStatus.OK) ;
    }
}
