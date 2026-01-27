package com.rud3xl.sbp.dto.user;


import com.rud3xl.sbp.domain.enums.UserStatus;
import lombok.*;

import java.util.UUID;

@Data
public class UserResponse {
    UUID id;
    String email;
    String fullName;
    UserStatus status;
}
