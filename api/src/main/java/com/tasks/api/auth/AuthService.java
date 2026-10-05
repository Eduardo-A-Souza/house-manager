package com.tasks.api.auth;

import com.tasks.api.auth.dto.LoginRequest;
import com.tasks.api.auth.dto.LoginResponse;
import com.tasks.api.auth.dto.RegisterRequest;
import com.tasks.api.auth.dto.RegisterResponse;
import com.tasks.api.exception.EmailAlreadyUsedException;
import com.tasks.api.security.JwtService;
import com.tasks.api.user.User;
import com.tasks.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException(email);
        }

        User user = new User();
        user.setEmail(email);
        user.setName(request.name().trim());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setHouseId(null);

        User saved = userRepository.save(user);
        return new RegisterResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid Email or Password"));

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }

}
