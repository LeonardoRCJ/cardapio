package com.leo.cardapio.model.food.dtos;

import com.leo.cardapio.model.food.FoodType;

public record UpdateFoodDTO(String title, String image, String description, Double price, FoodType type) {
}
