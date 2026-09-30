package com.tasks.api.auth;

import com.tasks.api.exception.EmailAlreadyUsedException;
import com.tasks.api.user.User;
import com.tasks.api.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
}
