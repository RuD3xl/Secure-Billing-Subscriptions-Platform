package com.rud3xl.sbp.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "The field must not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "The field must not be empty")
    private String password;
}
