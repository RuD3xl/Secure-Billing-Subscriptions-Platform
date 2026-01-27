package com.rud3xl.sbp.mapper;

import com.rud3xl.sbp.domain.UserEntity;
import com.rud3xl.sbp.dto.user.UserResponse;
import com.rud3xl.sbp.dto.auth.RegisterRequest;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true)) // <--- ВАЖНО: Делает из маппера Spring Bean
public interface UserMapper {

    // MapStruct сам поймет: "Ага, взять поле email из user и положить в email в DTO"
    UserResponse toDto(UserEntity user);

    // Обратная конвертация (например, при регистрации)
    // ignore = true значит "не трогай это поле, я сам его заполню или оно пустое"
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true) // Пароль мы хешируем отдельно
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserEntity toEntity(RegisterRequest request);
}