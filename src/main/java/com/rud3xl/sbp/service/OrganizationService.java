package com.rud3xl.sbp.service;


import com.rud3xl.sbp.domain.Membership;
import com.rud3xl.sbp.domain.Organization;
import com.rud3xl.sbp.domain.UserEntity;
import com.rud3xl.sbp.domain.enums.MembershipStatus;
import com.rud3xl.sbp.domain.enums.OrganizationRole;
import com.rud3xl.sbp.domain.enums.OrganizationStatus;
import com.rud3xl.sbp.dto.organization.CreateOrganizationRequest;
import com.rud3xl.sbp.dto.organization.OrganizationResponse;
import com.rud3xl.sbp.dto.organization.UpdateOrganizationRequest;
import com.rud3xl.sbp.exception.OrganizationExistsException;
import com.rud3xl.sbp.exception.ResourceNotFoundException;
import com.rud3xl.sbp.mapper.OrganizationMapper;
import com.rud3xl.sbp.repository.MembershipRepository;
import com.rud3xl.sbp.repository.OrganizationRepository;
import com.rud3xl.sbp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final OrganizationMapper organizationMapper;

    @Transactional
    public OrganizationResponse createOrganization(CreateOrganizationRequest request, String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = request.getName().toLowerCase().replace(" ", "-");
        }

        if (organizationRepository.existsBySlug(slug)) {
            throw new OrganizationExistsException("Organization with slug " + slug + " already exists.");
        }
        Organization organization = Organization.builder()
                .name(request.getName())
                .slug(slug)
                .status(OrganizationStatus.ACTIVE)
                .build();
        organizationRepository.save(organization);
        Membership membership = Membership.builder()
                .organization(organization)
                .user(user)
                .status(MembershipStatus.ACTIVE)
                .role(OrganizationRole.OWNER)
                .build();
        membershipRepository.save(membership);

        return organizationMapper.toDto(organization, OrganizationRole.OWNER);
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationBySlug(String email, String slug){
        Membership membership = getMembershipOrThrow(email, slug);
        return organizationMapper.toDto(membership.getOrganization(), membership.getRole());
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAllMyOrganization(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not fund"));
        List<Membership> membership = membershipRepository.findAllByUserId(user.getId());
        return membership.stream()
                .map(m -> organizationMapper.toDto(
                        m.getOrganization(),
                        m.getRole()
                ))
                .toList();
    }

    @Transactional
    public OrganizationResponse updateOrganization(String slug, UpdateOrganizationRequest request, String email){
        Membership membership = getMembershipOrThrow(email, slug);
        Organization organization = membership.getOrganization();
        if(membership.getRole() != OrganizationRole.OWNER && membership.getRole() != OrganizationRole.ADMIN){
            throw new AccessDeniedException("User is not allowed to update organization");
        }
        if(request.getName() != null){
            organization.setName(request.getName());
        }

        if(request.getSlug() != null && !organization.getSlug().equals(request.getSlug())){
            if (organizationRepository.existsBySlug(request.getSlug())) {
                throw new OrganizationExistsException("Organization with slug " + request.getSlug() + " already exists.");
            }
            organization.setSlug(request.getSlug());
        }
        return organizationMapper.toDto(organization, membership.getRole());
    }

    @Transactional
    public void deleteOrganization(String slug, String email){
        Membership membership = getMembershipOrThrow(email, slug);
        if(membership.getRole() != OrganizationRole.OWNER){
            throw new AccessDeniedException("User is not allowed to delete organization");
        }
        Organization organization = membership.getOrganization();
        organization.setStatus(OrganizationStatus.SUSPENDED);
        organizationRepository.save(organization);
    }


    private Membership getMembershipOrThrow(String email, String orgSlug) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Organization organization = organizationRepository.findBySlug(orgSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        return membershipRepository.findByUserIdAndOrganizationId(user.getId(), organization.getId())
                .orElseThrow(() -> new AccessDeniedException("Access denied: You are not a member of this organization"));
    }
}
