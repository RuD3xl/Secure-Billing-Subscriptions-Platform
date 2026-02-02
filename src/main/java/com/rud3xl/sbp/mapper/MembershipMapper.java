package com.rud3xl.sbp.mapper;

import com.rud3xl.sbp.domain.Membership;
import com.rud3xl.sbp.dto.organization.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembershipMapper {
    @Mapping(target = "name", source = "membership.user.fullName")
    @Mapping(target = "email", source = "membership.user.email")
    @Mapping(target = "role", source = "membership.role") // с этой темой я кста разобрался
    @Mapping(target = "id", source = "membership.user.id")
    @Mapping(target = "status", source = "membership.status")
    @Mapping(target = "joinedAt", source = "membership.createdAt")
    MemberResponse toDto(Membership membership);
}
