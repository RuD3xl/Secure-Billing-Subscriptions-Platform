package com.rud3xl.sbp.mapper;

import com.rud3xl.sbp.domain.UserEntity;
import com.rud3xl.sbp.dto.user.UserResponse;
import com.rud3xl.sbp.dto.auth.RegisterRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface UserMapper {

    UserResponse toDto(UserEntity user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true) // Пароль мы хешируем отдельно
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserEntity toEntity(RegisterRequest request);
}