package com.leo.cardapio.model.food.dtos;

import com.leo.cardapio.model.food.Food;

public record FoodRequestDTO(String title, String image, String description, Double price) {
    public Food toEntity(){
        return new Food(null, title, image, description, price);
    }
}
