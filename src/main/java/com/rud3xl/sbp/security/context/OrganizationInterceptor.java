package com.rud3xl.sbp.security.context;


import com.rud3xl.sbp.domain.Membership;
import com.rud3xl.sbp.domain.Organization;
import com.rud3xl.sbp.domain.enums.MembershipStatus;
import com.rud3xl.sbp.exception.ResourceNotFoundException;
import com.rud3xl.sbp.repository.MembershipRepository;
import com.rud3xl.sbp.repository.OrganizationRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor
public class OrganizationInterceptor implements HandlerInterceptor {
    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("X-Org-Slug");
        if (header == null || header.isBlank()) {
            return true;
        }
        Organization organization = organizationRepository.findBySlug(header).orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()){
            throw new AccessDeniedException("You are not authenticated");
        }
        String email = auth.getName();
        if(!membershipRepository.existsByOrganizationIdAndUserEmailAndStatus(organization.getId(), email, MembershipStatus.ACTIVE)){
            throw new AccessDeniedException("You are not a member of this organization");
        }
        OrganizationContext.setOrganization(organization);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        OrganizationContext.clearOrganization();
    }
}
