package com.rud3xl.sbp.domain;

import com.rud3xl.sbp.domain.enums.MembershipStatus;
import com.rud3xl.sbp.domain.enums.OrganizationRole;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "memberships", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "organization_id"}))
@Entity
public class Membership extends AbstractEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrganizationRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;
}
