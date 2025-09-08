package com.leo.cardapio.model.user.exceptions;

public class UserAlreadyExistsWithPhoneException extends RuntimeException {
    public UserAlreadyExistsWithPhoneException(String message) {
        super(message);
    }
}
