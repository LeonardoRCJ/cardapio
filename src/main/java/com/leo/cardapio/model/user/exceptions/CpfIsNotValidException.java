package com.leo.cardapio.model.user.exceptions;

public class CpfIsNotValidException extends RuntimeException {
    public CpfIsNotValidException(String message) {
        super(message);
    }
}
