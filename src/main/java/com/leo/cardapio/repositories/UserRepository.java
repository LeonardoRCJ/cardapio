package com.leo.cardapio.repositories;

import com.leo.cardapio.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByPhone(String phone);
    boolean existsByCpf(String cpf);
}
