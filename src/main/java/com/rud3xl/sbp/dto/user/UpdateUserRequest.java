package com.rud3xl.sbp.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(min = 3, max = 30, message = "Full name must be between 3 and 30 characters long")
    private String fullName;
}
