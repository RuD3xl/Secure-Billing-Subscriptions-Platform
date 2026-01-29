package com.rud3xl.sbp.dto.organization;

import com.rud3xl.sbp.domain.enums.MembershipStatus;
import com.rud3xl.sbp.domain.enums.OrganizationRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private UUID id;
    private String email;
    private String name;
    private OrganizationRole role;
    private MembershipStatus status;
    private LocalDateTime joinedAt;
}
