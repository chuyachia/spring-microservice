package com.chuya.userservice.controller;

import com.chuya.common.model.User;
import com.chuya.userservice.model.UserVO;
import com.chuya.userservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    public User saveUser(User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public UserVO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
