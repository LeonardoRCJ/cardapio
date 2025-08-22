package com.leo.cardapio.config;

import com.leo.cardapio.model.food.exceptions.FoodNotFoundException;
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
}
