package com.leo.cardapio.model.user.exceptions;

public class UserAlreadyExistsWithCpfException extends RuntimeException {
    public UserAlreadyExistsWithCpfException(String message) {
        super(message);
    }
}
