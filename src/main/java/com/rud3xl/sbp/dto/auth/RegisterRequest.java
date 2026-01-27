package com.rud3xl.sbp.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
public class RegisterRequest {
    @NotBlank(message = "The field must not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "The field must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter and one digit")
    private String password;

    @Size(min = 2, max = 24, message = "Full name must be between 2 and 24 characters long")
    private String fullName;
}
