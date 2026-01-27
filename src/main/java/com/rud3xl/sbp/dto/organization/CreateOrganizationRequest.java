package com.rud3xl.sbp.dto.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequest {
    @NotBlank(message = "The field must not be empty")
    @Size(min = 3, max = 30, message = "Organization name must be between 3 and 30 characters long")
    private String name;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug can only contain lowercase letters, numbers and hyphens")
    private String slug;
}
