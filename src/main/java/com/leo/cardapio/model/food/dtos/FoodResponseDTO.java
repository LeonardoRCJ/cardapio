package com.leo.cardapio.model.food.dtos;

import com.leo.cardapio.model.food.Food;
import com.leo.cardapio.model.food.FoodType;

public record FoodResponseDTO(Long id, String title, String image, String description, Double price, FoodType type) {
    public FoodResponseDTO(Food food){
        this(food.getId(), food.getTitle(), food.getImage(), food.getDescription(), food.getPrice(), food.getType());
    }
}
