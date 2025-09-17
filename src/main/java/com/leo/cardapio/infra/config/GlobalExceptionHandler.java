package com.leo.cardapio.infra.config;

import com.leo.cardapio.model.food.exceptions.FoodNotFoundException;
import com.leo.cardapio.model.order.exceptions.PaymentException;
import com.leo.cardapio.model.user.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

record ExceptionDTO(String message) {}


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FoodNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleFoodNotFound(FoodNotFoundException ex) {
        ExceptionDTO response = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleUserNotFound(UserNotFoundException ex) {
        ExceptionDTO response = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsWithCpfException.class)
    public ResponseEntity<ExceptionDTO> handleUserAlreadyExistsByCpf(UserAlreadyExistsWithCpfException ex) {
        ExceptionDTO response = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(UserAlreadyExistsWithPhoneException.class)
    public ResponseEntity<ExceptionDTO> handleUserAlreadyExistsByPhone(UserAlreadyExistsWithPhoneException ex) {
        ExceptionDTO response = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsWithEmailException.class)
    public ResponseEntity<ExceptionDTO> handleUserAlreadyExistsWithEmail(UserAlreadyExistsWithEmailException ex) {
        ExceptionDTO response = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(CpfIsNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleCpfIsNotValid(CpfIsNotValidException ex) {
        ExceptionDTO response = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PaymentException.class)
    public  ResponseEntity<ExceptionDTO> handlePaymentException(PaymentException ex) {
        ExceptionDTO response = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
