package com.leo.cardapio.model.user.dtos;

public record AuthResponse(String token, String name) {
    public AuthResponse(AuthResponse login) {
        this(login.token(), login.name());
    }
}
