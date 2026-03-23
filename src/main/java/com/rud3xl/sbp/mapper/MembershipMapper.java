package com.rud3xl.sbp.mapper;

import com.rud3xl.sbp.domain.Membership;
import com.rud3xl.sbp.dto.organization.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembershipMapper {
    @Mapping(target = "name", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "joinedAt", source = "createdAt")
    MemberResponse toDto(Membership membership);
}
