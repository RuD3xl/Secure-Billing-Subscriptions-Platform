package com.rud3xl.sbp.repository;

import com.rud3xl.sbp.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsBySlug(String slug);
    Optional<Organization> findBySlug(String slug);
}
