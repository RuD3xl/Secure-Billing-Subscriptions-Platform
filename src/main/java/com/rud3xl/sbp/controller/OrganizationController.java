package com.rud3xl.sbp.controller;

import com.rud3xl.sbp.dto.organization.CreateOrganizationRequest;
import com.rud3xl.sbp.dto.organization.OrganizationResponse;
import com.rud3xl.sbp.dto.organization.UpdateOrganizationRequest;
import com.rud3xl.sbp.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/orgs")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;


    @PostMapping()
    public ResponseEntity<OrganizationResponse> createOrganization(
            @RequestBody @Valid CreateOrganizationRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        OrganizationResponse response = organizationService.createOrganization(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<OrganizationResponse>> getAllMyOrganization(@AuthenticationPrincipal UserDetails userDetails){
        List<OrganizationResponse> response = organizationService.getAllMyOrganization(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<OrganizationResponse> getOrganizationBySlug(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String slug){
        OrganizationResponse response = organizationService.getOrganizationBySlug(userDetails.getUsername(), slug);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @PathVariable String slug,
            @RequestBody @Valid UpdateOrganizationRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(organizationService.updateOrganization(slug, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String slug, @AuthenticationPrincipal UserDetails userDetails){
        organizationService.deleteOrganization(slug, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}


