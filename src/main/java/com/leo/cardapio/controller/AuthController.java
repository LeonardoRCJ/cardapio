package com.leo.cardapio.controller;

import com.leo.cardapio.model.user.dtos.AuthRequest;
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
    public ResponseEntity<String> login(@RequestBody AuthRequest data) {
        String token = authService.login(data);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO data) {
        UUID id = authService.register(data);

        return ResponseEntity.created(URI.create("/admin/users/" + id)).build();
    }
}
