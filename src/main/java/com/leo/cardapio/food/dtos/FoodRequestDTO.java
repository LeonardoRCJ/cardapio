package com.leo.cardapio.food.dtos;

import com.leo.cardapio.food.Food;

public record FoodRequestDTO(String title, String image, Double price) {
    public Food toEntity(){
        return new Food(null, title, image, price);
    }
}
