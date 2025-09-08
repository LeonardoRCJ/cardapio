package com.leo.cardapio.model.user.exceptions;

public class UserAlreadyExistsWithEmailException extends RuntimeException {
    public UserAlreadyExistsWithEmailException(String message) {
        super(message);
    }
}
