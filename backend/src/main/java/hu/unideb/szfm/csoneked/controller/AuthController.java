package hu.unideb.szfm.csoneked.controller;

import hu.unideb.szfm.csoneked.dto.ApiResponse;
import hu.unideb.szfm.csoneked.dto.LoginRequest;
import hu.unideb.szfm.csoneked.dto.RegisterRequest;
import hu.unideb.szfm.csoneked.dto.UserDto;
import hu.unideb.szfm.csoneked.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDto>> login(@Valid @RequestBody LoginRequest request) {
        UserDto user = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Sikeres bejelentkezés!", user));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody RegisterRequest request) {
        UserDto user = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Sikeres regisztráció! 15 000 Hc kezdő egyenleget jóváírtunk.", user));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
        UserDto user = authService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = authService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok(users));
    }
}
