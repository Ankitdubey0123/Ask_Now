package in.ankit.main.auth.controller;

import in.ankit.main.auth.dto.*;
import in.ankit.main.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asknow/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/test")
    public String test() {
        return "API WORKING";
    }

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody RegisterDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody LoginDto request) {
        System.out.println("**************** LOGIN CONTROLLER HIT ****************");
        return authService.login(request);
    }

    @GetMapping("/current-user")
    public AuthResponseDto getCurrentUser() {
        return authService.getCurrentUser();
    }
}
