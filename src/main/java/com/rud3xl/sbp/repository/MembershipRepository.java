package com.rud3xl.sbp.repository;

import com.rud3xl.sbp.domain.Membership;
import com.rud3xl.sbp.domain.enums.MembershipStatus;
import com.rud3xl.sbp.domain.enums.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    List<Membership> findAllByUserId(UUID userId);
    Optional<Membership> findByUserIdAndOrganizationIdAndStatus(UUID userId, UUID organizationId, MembershipStatus status);
    Optional<Membership> findByUserIdAndOrganizationId(UUID userId, UUID organizationId);
    List<Membership> findAllByOrganizationId(UUID organizationId);
    Optional<Membership> findByUserEmailAndOrganizationId(String email, UUID orgId);
    Long countByOrganizationIdAndRoleAndStatus(UUID organizationId, OrganizationRole role, MembershipStatus status);
    boolean existsByOrganizationIdAndUserEmailAndStatus(UUID organizationId, String userEmail, MembershipStatus status);
}
