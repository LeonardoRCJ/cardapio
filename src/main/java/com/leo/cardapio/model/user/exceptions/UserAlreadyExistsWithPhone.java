package com.leo.cardapio.model.user.exceptions;

public class UserAlreadyExistsWithPhone extends RuntimeException {
    public UserAlreadyExistsWithPhone(String message) {
        super(message);
    }
}
