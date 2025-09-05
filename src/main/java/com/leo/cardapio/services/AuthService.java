package com.leo.cardapio.services;

import com.leo.cardapio.infra.security.TokenService;
import com.leo.cardapio.model.user.User;
import com.leo.cardapio.model.user.dtos.AuthRequest;
import com.leo.cardapio.model.user.dtos.UserRequestDTO;
import com.leo.cardapio.model.user.exceptions.UserAlreadyExistsWithCpfException;
import com.leo.cardapio.model.user.exceptions.UserNotFoundException;
import com.leo.cardapio.repositories.UserRepository;
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

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, UserRepository repository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(AuthRequest data){
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        var email = ((UserDetails) authentication.getPrincipal()).getUsername();
        var user = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("Usuário não foi encontrado com o email: " + email));

        return tokenService.generateToken(user);
    }

    public UUID register(UserRequestDTO data) {
        if (repository.existsByCpf(data.cpf())) {
            throw new UserAlreadyExistsWithCpfException("Usuário com o cpf: " + data.cpf() + " já existe");
        }
        if (repository.existsByPhone(data.phone())) {
            throw new UserAlreadyExistsWithCpfException("Usuário com o telefone: " + data.phone() + " já existe");
        }

        User user = data.toEntity();

        return repository.save(user).getId();
    }
}
