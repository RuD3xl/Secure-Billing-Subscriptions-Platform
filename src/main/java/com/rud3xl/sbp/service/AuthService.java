package com.rud3xl.sbp.service;


import com.rud3xl.sbp.domain.UserEntity;
import com.rud3xl.sbp.domain.enums.SystemRole;
import com.rud3xl.sbp.domain.enums.UserStatus;
import com.rud3xl.sbp.dto.auth.AuthResponse;
import com.rud3xl.sbp.dto.auth.LoginRequest;
import com.rud3xl.sbp.dto.auth.RegisterRequest;
import com.rud3xl.sbp.repository.UserRepository;
import com.rud3xl.sbp.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest registerRequest){
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity user = UserEntity.builder()
                .email(registerRequest.getEmail())
                .role(SystemRole.ROLE_USER)
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .status(UserStatus.PENDING_VERIFICATION)
                .fullName(registerRequest.getFullName()).build();
        userRepository.save(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest){

        UserEntity user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())){
            throw new BadCredentialsException("Invalid email or password");
        }
        return AuthResponse.builder().token(jwtService.generateToken(user)).build();
    }
}
