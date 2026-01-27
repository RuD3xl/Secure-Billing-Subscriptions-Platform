package com.rud3xl.sbp.controller;

import com.rud3xl.sbp.dto.user.UpdateUserRequest;
import com.rud3xl.sbp.dto.user.UserResponse;
import com.rud3xl.sbp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getProfile(@AuthenticationPrincipal UserDetails userDetails){
        return userService.getUserByEmail(userDetails.getUsername());
    }

    @PatchMapping("/me")
    public UserResponse updateUser(@AuthenticationPrincipal UserDetails userDetails,@RequestBody @Valid UpdateUserRequest userRequest){
        return userService.updateUser(userDetails.getUsername(),userRequest);
    }

}
