package com.leo.cardapio.model.user.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leo.cardapio.model.user.User;
import com.leo.cardapio.model.user.UserType;

import java.time.LocalDate;

public record UserRequestDTO(String fullname, String cpf, String phone, String password, LocalDate birthdate, String email, UserType role) {

    public User toEntity() {
        return new User(null, fullname, cpf, email, phone, birthdate, password, role);
    }
}