package com.rud3xl.sbp.mapper;

import com.rud3xl.sbp.domain.Organization;
import com.rud3xl.sbp.domain.enums.OrganizationRole;
import com.rud3xl.sbp.dto.organization.OrganizationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "role", source = "role")
    @Mapping(target = "id", source = "organization.id")
    OrganizationResponse toDto(Organization organization, OrganizationRole role);
}
