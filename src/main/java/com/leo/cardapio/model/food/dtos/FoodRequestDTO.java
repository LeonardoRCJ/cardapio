package com.leo.cardapio.model.food.dtos;

import com.leo.cardapio.model.food.Food;
import com.leo.cardapio.model.food.FoodType;

public record FoodRequestDTO(String title, String image, String description, Double price, FoodType type) {
    public Food toEntity(){
        return new Food(null, title, image, description, price, type);
    }
}
