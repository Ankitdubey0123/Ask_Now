package in.ankit.main.auth.services;

import in.ankit.main.auth.dto.*;
import in.ankit.main.auth.entities.Role;
import in.ankit.main.auth.entities.User;
import in.ankit.main.auth.repository.UserRepository;
import in.ankit.main.security.jwtServices.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        boolean isTeacher = "TEACHER".equalsIgnoreCase(request.getRole());
        Set<Role> roles = isTeacher ? Set.of(Role.TEACHER) : Set.of(Role.STUDENT);

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .enabled(true)
                .build();

        userRepository.save(user);

        String role = user.getRoles().iterator().next().name();

        String token = jwtService.generateToken(
                user.getEmail(),
                role
        );

        return AuthResponseDto.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(role)
                .build();
    }

    public AuthResponseDto login(LoginDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account is disabled or pending verification.");
        }

        String role = user.getRoles().iterator().next().name();

        String token = jwtService.generateToken(
                user.getEmail(),
                role
        );

        return AuthResponseDto.builder()
                .id(user.getId())
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(role)
                .build();
    }

    public AuthResponseDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRoles().iterator().next().name();
        return AuthResponseDto.builder()
                .id(user.getId())
                .token("")
                .email(user.getEmail())
                .name(user.getName())
                .role(role)
                .build();
    }
}
