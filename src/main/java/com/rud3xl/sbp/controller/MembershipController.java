package com.rud3xl.sbp.controller;

import com.rud3xl.sbp.domain.Organization;
import com.rud3xl.sbp.dto.organization.AddMemberRequest;
import com.rud3xl.sbp.dto.organization.MemberResponse;
import com.rud3xl.sbp.dto.organization.UpdateMemberRoleRequest;
import com.rud3xl.sbp.security.context.CurrentOrg;
import com.rud3xl.sbp.service.Organizations.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orgs/{slug}/members")
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @GetMapping()
    public ResponseEntity<List<MemberResponse>> getOrganizationMemberships(
            @AuthenticationPrincipal UserDetails userDetails,
            @CurrentOrg Organization organization
    ){
        List<MemberResponse> memberResponses = membershipService.getOrganizationMembers(userDetails.getUsername(), organization);
        return ResponseEntity.ok(memberResponses);
    }

    @PostMapping()
    public ResponseEntity<MemberResponse> inviteMember(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddMemberRequest request,
            @CurrentOrg Organization organization
    ){
        MemberResponse response = membershipService.addMember(organization, userDetails.getUsername(), request.getEmail());
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(
            @RequestBody UpdateMemberRoleRequest request,
            @PathVariable UUID memberId,
            @AuthenticationPrincipal UserDetails userDetails,
            @CurrentOrg Organization organization
    ) {
        MemberResponse response = membershipService.updateMemberRole(userDetails.getUsername(), organization, memberId, request.getRole());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID memberId,
            @AuthenticationPrincipal UserDetails userDetails,
            @CurrentOrg Organization organization
    ) {
        membershipService.removeMember(userDetails.getUsername(), organization, memberId);
        return ResponseEntity.noContent().build();
    }
}