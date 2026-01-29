package com.rud3xl.sbp.service.Organizations;

import com.rud3xl.sbp.domain.Membership;
import com.rud3xl.sbp.domain.Organization;
import com.rud3xl.sbp.domain.UserEntity;
import com.rud3xl.sbp.domain.enums.MembershipStatus;
import com.rud3xl.sbp.domain.enums.OrganizationRole;
import com.rud3xl.sbp.dto.organization.MemberResponse;
import com.rud3xl.sbp.exception.ResourceExistsException;
import com.rud3xl.sbp.exception.ResourceNotFoundException;
import com.rud3xl.sbp.mapper.MembershipMapper;
import com.rud3xl.sbp.repository.MembershipRepository;
import com.rud3xl.sbp.repository.OrganizationRepository;
import com.rud3xl.sbp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;


    public List<MemberResponse> getOrganizationMembers(String requesterEmail, String organizationSlug) {
        Membership requester = getMembershipOrThrow(requesterEmail, organizationSlug);
        return membershipRepository.findAllByOrganizationId(requester.getOrganization().getId())
                .stream()
                .filter(m -> m.getStatus() != MembershipStatus.REMOVED)
                .map(membershipMapper::toDto)
                .toList();
    }

    public MemberResponse addMember(String requesterEmail, String newMemberEmail, String organizationSlug) {
        Membership requester = getMembershipOrThrow(requesterEmail, organizationSlug);

        if (requester.getRole() != OrganizationRole.OWNER && requester.getRole() != OrganizationRole.ADMIN) {
            throw new AccessDeniedException("User is not allowed to invite to the organization");
        }

        Organization organization = requester.getOrganization();
        UserEntity newMemberUser = userRepository.findByEmail(newMemberEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<Membership> existingWrapper = membershipRepository
                .findByUserIdAndOrganizationId(newMemberUser.getId(), organization.getId());

        if (existingWrapper.isPresent()) {
            Membership targetMembership = existingWrapper.get();
            switch (targetMembership.getStatus()) {
                case ACTIVE -> throw new ResourceExistsException("User is already a member");
                case INVITED -> throw new ResourceExistsException("User has already been invited");
                case REMOVED -> {
                    targetMembership.setStatus(MembershipStatus.ACTIVE);
                    targetMembership.setRole(OrganizationRole.MEMBER);
                    membershipRepository.save(targetMembership);
                    return membershipMapper.toDto(targetMembership);
                }
            }
        }

        Membership newMembership = Membership.builder()
                .user(newMemberUser)
                .organization(organization)
                .role(OrganizationRole.MEMBER)
                .status(MembershipStatus.ACTIVE)
                .build();

        membershipRepository.save(newMembership);
        return membershipMapper.toDto(newMembership);
    }

    public void removeMember(String requesterEmail, String organizationSlug, UUID targetUserId) {
        Membership requester = getMembershipOrThrow(requesterEmail, organizationSlug);
        if (requester.getRole() != OrganizationRole.OWNER && requester.getRole() != OrganizationRole.ADMIN) {
            throw new AccessDeniedException("Insufficient permissions to remove members");
        }
        if (requester.getUser().getId().equals(targetUserId)) {
            throw new AccessDeniedException("You cannot remove yourself");
        }

        Membership target = getMembershipByUserIdOrThrow(targetUserId, requester.getOrganization().getId());

        if (requester.getRole() == OrganizationRole.ADMIN && target.getRole() == OrganizationRole.OWNER) {
            throw new AccessDeniedException("Admin cannot remove Owner");
        }

        if (target.getRole() == OrganizationRole.OWNER) {
            long ownersCount = membershipRepository.countByOrganizationIdAndRoleAndStatus(
                    target.getOrganization().getId(),
                    OrganizationRole.OWNER,
                    MembershipStatus.ACTIVE
            );

            if (ownersCount <= 1) {
                throw new AccessDeniedException("Cannot remove the last OWNER of the organization.");
            }
        }
        target.setStatus(MembershipStatus.REMOVED);
        membershipRepository.save(target);
    }

    public MemberResponse updateMemberRole(String requesterEmail, String organizationSlug, UUID targetUserId, OrganizationRole newRole) {
        Membership requester = getMembershipOrThrow(requesterEmail, organizationSlug);

        if (requester.getRole() != OrganizationRole.OWNER && requester.getRole() != OrganizationRole.ADMIN) {
            throw new AccessDeniedException("Insufficient permissions to update roles");
        }
        if (requester.getUser().getId().equals(targetUserId)) {
            throw new AccessDeniedException("You cannot change your own role");
        }
        Membership target = getMembershipByUserIdOrThrow(targetUserId, requester.getOrganization().getId());

        if (requester.getRole() == OrganizationRole.ADMIN && target.getRole() == OrganizationRole.OWNER) {
            throw new AccessDeniedException("Admin cannot change Owner's role");
        }

        target.setRole(newRole);
        membershipRepository.save(target);
        return membershipMapper.toDto(target);
    }

    private Membership getMembershipOrThrow(String email, String orgSlug) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Organization organization = organizationRepository.findBySlug(orgSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        return membershipRepository.findByUserIdAndOrganizationIdAndStatus(user.getId(), organization.getId(), MembershipStatus.ACTIVE)
                .orElseThrow(() -> new AccessDeniedException("Access denied: You are not a member of this organization"));
    }
    private Membership getMembershipByUserIdOrThrow(UUID userId, UUID orgId) {
        return membershipRepository.findByUserIdAndOrganizationIdAndStatus(userId, orgId, MembershipStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in this organization"));
    }
}
