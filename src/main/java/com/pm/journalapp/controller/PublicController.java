package com.pm.journalapp.controller;

import com.pm.journalapp.entity.User;
import com.pm.journalapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }


    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        userService.saveNewUser(user);
    }
}
