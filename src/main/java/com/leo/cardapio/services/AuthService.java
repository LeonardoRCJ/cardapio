package com.leo.cardapio.services;

import com.leo.cardapio.infra.security.TokenService;
import com.leo.cardapio.model.user.User;
import com.leo.cardapio.model.user.dtos.AuthRequest;
import com.leo.cardapio.model.user.dtos.UserRequestDTO;
import com.leo.cardapio.model.user.exceptions.CpfIsNotValidException;
import com.leo.cardapio.model.user.exceptions.UserAlreadyExistsWithCpfException;
import com.leo.cardapio.model.user.exceptions.UserAlreadyExistsWithEmailException;
import com.leo.cardapio.model.user.exceptions.UserNotFoundException;
import com.leo.cardapio.repositories.UserRepository;
import com.leo.cardapio.validators.CpfValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CpfValidator cpfValidator;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, UserRepository repository, PasswordEncoder passwordEncoder, CpfValidator cpfValidator) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.cpfValidator = cpfValidator;
    }

    public String login(AuthRequest data){
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var email = ((UserDetails) authentication.getPrincipal()).getUsername();
        var user = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("Usuário não foi encontrado com o email: " + email));

        return tokenService.generateToken(user);
    }

    public UUID register(UserRequestDTO data) {
        if (!cpfValidator.isValid(data.cpf())) {
            throw new CpfIsNotValidException("CPF: " + data.cpf() + " não é válido, verifique e tente novamente");
        }

        if (repository.existsByEmail(data.email())) {
            throw new UserAlreadyExistsWithEmailException("Email: " + data.email() + " já existe no sistema");
        }

        if (repository.existsByCpf(data.cpf())) {
            throw new UserAlreadyExistsWithCpfException("Usuário com o cpf: " + data.cpf() + " já existe no sistema");
        }

        if (repository.existsByPhone(data.phone())) {
            throw new UserAlreadyExistsWithCpfException("Usuário com o telefone: " + data.phone() + " já existe no sistema");
        }
        User user = new User();
        user.setCpf(data.cpf());
        user.setEmail(data.email());
        user.setRole(data.role());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setFullname(data.fullname());
        user.setBirthdate(data.birthdate());
        user.setPhone(data.phone());


        return repository.save(user).getId();
    }
}
