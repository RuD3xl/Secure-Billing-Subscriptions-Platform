package com.rud3xl.sbp.dto.organization;

import com.rud3xl.sbp.domain.enums.OrganizationRole;
import com.rud3xl.sbp.domain.enums.OrganizationStatus;
import lombok.Builder;
import lombok.Data;


import java.util.UUID;

@Builder
@Data
public class OrganizationResponse {
    private UUID id;
    private String name;
    private String slug;
    private OrganizationStatus status;
    private OrganizationRole role;
}
