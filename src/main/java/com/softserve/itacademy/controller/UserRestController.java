package com.softserve.itacademy.controller;

import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.dto.userDto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @Secured("ADMIN")
    @GetMapping("")
    public List<UserDto> users(Principal principal) {
        return userService.findAll();
    }

}
