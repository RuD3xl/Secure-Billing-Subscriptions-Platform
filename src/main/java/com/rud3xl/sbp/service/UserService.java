package com.rud3xl.sbp.service;

import com.rud3xl.sbp.domain.UserEntity;
import com.rud3xl.sbp.dto.user.UpdateUserRequest;
import com.rud3xl.sbp.dto.user.UserResponse;
import com.rud3xl.sbp.exception.ResourceNotFoundException;
import com.rud3xl.sbp.mapper.UserMapper;
import com.rud3xl.sbp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserResponse getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @Transactional
    public UserResponse updateUser(String email, UpdateUserRequest userRequest) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (userRequest.getFullName() != null) {
            userEntity.setFullName(userRequest.getFullName());
        }
        UserEntity updatedUser = userRepository.save(userEntity);
        return userMapper.toDto(updatedUser);
    }
}
