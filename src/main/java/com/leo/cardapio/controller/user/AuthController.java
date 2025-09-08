package com.leo.cardapio.controller.user;

import com.leo.cardapio.model.user.dtos.AuthRequest;
import com.leo.cardapio.model.user.dtos.AuthResponse;
import com.leo.cardapio.model.user.dtos.UserRequestDTO;
import com.leo.cardapio.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest data) {
        AuthResponse response = new AuthResponse(authService.login(data));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO data) {
        UUID id = authService.register(data);

        return ResponseEntity.created(URI.create("/admin/users/" + id)).build();
    }
}
