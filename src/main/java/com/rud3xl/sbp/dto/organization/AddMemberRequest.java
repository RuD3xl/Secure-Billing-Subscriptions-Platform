package com.rud3xl.sbp.dto.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMemberRequest {
    @NotBlank(message = "The field must not be empty")
    @Email(message = "Invalid email format")
    private String email;
}
