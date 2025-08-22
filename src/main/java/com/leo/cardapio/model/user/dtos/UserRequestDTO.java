package com.leo.cardapio.model.user.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leo.cardapio.model.user.User;

import java.time.LocalDate;

public record UserRequestDTO(String fullname, String cpf, String phone, LocalDate birthdate) {

    public User toEntity() {
        return new User(null, fullname, cpf, phone, birthdate );
    }
}
