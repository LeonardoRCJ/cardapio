package com.leo.cardapio.model.user;

import com.leo.cardapio.model.user.dtos.UserRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fullname;
    @Column(unique = true, updatable = false)
    private String cpf;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    private LocalDate birthdate;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType role;
}